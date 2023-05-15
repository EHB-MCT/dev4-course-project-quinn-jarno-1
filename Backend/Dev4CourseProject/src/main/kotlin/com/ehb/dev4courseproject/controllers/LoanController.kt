package com.ehb.dev4courseproject.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import com.ehb.dev4courseproject.dto.NewLoanRequest
import com.ehb.dev4courseproject.models.Loan
import com.ehb.dev4courseproject.services.LoanService
import com.ehb.dev4courseproject.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.net.http.HttpHeaders

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("loans")
class LoanController {

    @Autowired
    lateinit var loanService: LoanService;

    // Get All Loans
    @GetMapping
    fun getAllLoans(): List<Loan> {
        val loans = loanService.getLoans();
        return loans;
    }

    // Create A New Loan
    @PostMapping
    fun loanItem(@RequestBody loanRequest: NewLoanRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) token: String): Loan {
        if(UserService.isAuthenticated(token)){
            return loanService.newLoan(loanRequest)
        } else {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication token not present / allowed")
        }
    }

    // Return Loan (Sets Item isLoanedOut to false and Loan Returned to True)
    @PostMapping("/return/{loanId}")
    fun returnLoan(@PathVariable loanId: Long) {
        loanService.returnLoan(loanId)
    }
}