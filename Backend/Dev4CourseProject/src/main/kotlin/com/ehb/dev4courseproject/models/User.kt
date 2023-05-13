package com.ehb.dev4courseproject.models

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1,

    var username: String?,
    var email: String?,
    var password: String?,
    var role: String?
){}

