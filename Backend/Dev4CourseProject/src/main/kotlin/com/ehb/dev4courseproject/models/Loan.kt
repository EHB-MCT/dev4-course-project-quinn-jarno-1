package com.ehb.dev4courseproject.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "loans")
data class Loan(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1,

    val loanDate: LocalDateTime = LocalDateTime.now(),
    val dueDate: LocalDateTime,
    var returned: Boolean = false,

    @ManyToOne
    val user: User,
    @OneToOne
    val item: Item,
){
}