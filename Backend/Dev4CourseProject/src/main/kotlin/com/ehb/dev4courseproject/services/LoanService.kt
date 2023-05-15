package com.ehb.dev4courseproject.services

import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

import com.ehb.dev4courseproject.dto.NewLoanRequest
import com.ehb.dev4courseproject.models.Loan
import com.ehb.dev4courseproject.repositories.LoanRepository
import com.ehb.dev4courseproject.repositories.UserRepository
import com.ehb.dev4courseproject.repositories.ItemRepository
@Service
class LoanService() {

    @Autowired
    lateinit var itemRepository: ItemRepository
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var loanRepository: LoanRepository

    fun getLoans(): List<Loan> {
        return loanRepository.findAll();
    }

    fun newLoan(loanRequest: NewLoanRequest): Loan {
        val user = userRepository.findById(loanRequest.userId)
            .orElseThrow { EntityNotFoundException("User not found with ID ${loanRequest.userId}") }

        val item = itemRepository.findById(loanRequest.itemId)
            .orElseThrow { EntityNotFoundException("Item not found with ID ${loanRequest.itemId}") }

        if (item.isLoanedOut) {
            throw EntityNotFoundException("Item with ID ${loanRequest.itemId} is already loaned out")
        }

        val loan = Loan(user = user, item = item, loanDate = LocalDateTime.now(), dueDate = LocalDateTime.now().plusDays(7))
        item.isLoanedOut = true

        return loanRepository.save(loan)
    }

    fun returnLoan(loanId: Long): Loan {
        val loan = loanRepository.findById(loanId)
            .orElseThrow { EntityNotFoundException("Loan not found with ID $loanId") }

        val item = loan.item
        item.isLoanedOut = false
        itemRepository.save(item)

        loan.returned = true
        return loanRepository.save(loan)
    }

    fun isAuthenticated(token: String): Boolean{
        val u = UserRepository.findByAccessToken(token)
        if(u != null) {
            println("User found!")
            return u.expirationDate > System.currentTimeMillis()
        } else {
            println("User not found!")
            return false
        }
    }
}