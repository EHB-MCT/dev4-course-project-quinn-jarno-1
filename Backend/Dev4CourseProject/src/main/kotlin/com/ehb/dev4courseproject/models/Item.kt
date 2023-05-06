package com.ehb.dev4courseproject.models

import jakarta.persistence.*

@Entity
@Table(name = "items")
data class Item(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1,

    var name: String = "",
    var description: String = "",
    var isLoanedOut: Boolean = false
){}