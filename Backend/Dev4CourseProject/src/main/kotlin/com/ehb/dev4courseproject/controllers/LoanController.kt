package com.ehb.dev4courseproject.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import com.ehb.dev4courseproject.dto.NewLoanRequest
import com.ehb.dev4courseproject.models.Loan
import com.ehb.dev4courseproject.services.LoanService

@RestController
@RequestMapping("loans")
class LoanController {

    @Autowired
    lateinit var loanService: LoanService;

    @GetMapping
    fun index(): List<Loan> {
        //Get all loans
        val loans = loanService.getLoans();

        return loans;
    }

    @PostMapping
    fun loanItem(@RequestBody loanRequest: NewLoanRequest): Loan {
        return loanService.newLoan(loanRequest)
    }

    @PostMapping("/return/{loanId}")
    fun returnLoan(@PathVariable loanId: Long) {
        loanService.returnLoan(loanId)
    }
}