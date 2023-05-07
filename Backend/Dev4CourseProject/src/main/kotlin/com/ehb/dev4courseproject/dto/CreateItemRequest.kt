package com.ehb.dev4courseproject.dto

data class CreateItemRequest(
    var name: String,
    var description: String,
    var isLoanedOut: Boolean
)
