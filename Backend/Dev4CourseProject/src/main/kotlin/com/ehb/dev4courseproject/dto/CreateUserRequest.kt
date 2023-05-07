package com.ehb.dev4courseproject.dto

data class CreateUserRequest(
    var username: String,
    var email: String,
    var password: String
)
