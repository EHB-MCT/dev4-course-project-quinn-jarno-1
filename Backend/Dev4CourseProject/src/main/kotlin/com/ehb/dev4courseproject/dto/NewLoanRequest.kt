package com.ehb.dev4courseproject.dto

import java.time.LocalDateTime
data class NewLoanRequest(
    val loanDate: LocalDateTime?,
    val dueDate: LocalDateTime?,
    val userId: Long,
    val itemId: Long,
    val returned: Boolean
)
