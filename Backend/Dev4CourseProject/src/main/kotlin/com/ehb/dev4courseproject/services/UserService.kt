package com.ehb.dev4courseproject.services

import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.ehb.dev4courseproject.models.User
import com.ehb.dev4courseproject.repositories.UserRepository
import com.ehb.dev4courseproject.dto.CreateUserRequest

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun createUser(user: CreateUserRequest): User {
        val user = User(username = user.username, email = user.email)
        return userRepository.save(user)
    }
}