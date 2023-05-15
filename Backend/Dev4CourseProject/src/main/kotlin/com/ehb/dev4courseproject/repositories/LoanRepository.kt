package com.ehb.dev4courseproject.repositories

import com.ehb.dev4courseproject.models.Loan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


interface LoanRepository : JpaRepository<Loan, Long> {
}