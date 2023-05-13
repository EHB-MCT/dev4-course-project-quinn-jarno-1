package com.ehb.dev4courseproject.services

import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.ehb.dev4courseproject.models.User
import com.ehb.dev4courseproject.repositories.UserRepository
import com.ehb.dev4courseproject.dto.CreateUserRequest
import com.ehb.dev4courseproject.repositories.LoanRepository

@Service
class UserService {

    @Autowired
    lateinit var loanRepository: LoanRepository

    @Autowired
    lateinit var userRepository: UserRepository

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun createUser(user: CreateUserRequest): User {
        val user = User(username = user.username, email = user.email, password = user.password, role = user.role)
        return userRepository.save(user)
    }

    fun getUser(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User not found with ID $userId") }
    }

    fun updateUser(userId: Long, userRequest: CreateUserRequest): User {
        val user = getUser(userId)
        user.username = userRequest.username
        user.email = userRequest.email
        user.password = userRequest.password
        return userRepository.save(user)
    }

    fun updateUserRole(userId: Long, userRequest: CreateUserRequest): User {
        val user = getUser(userId)
        user.role = userRequest.role
        return userRepository.save(user)
    }

    fun deleteUser(userId: Long): User {
        val user = userRepository.findById(userId)
        if (user.isPresent) {
            loanRepository.deleteById(userId)
            userRepository.deleteById(userId)
            return user.get()
        } else {
            throw EntityNotFoundException("User with id $userId not found")
        }
    }
}