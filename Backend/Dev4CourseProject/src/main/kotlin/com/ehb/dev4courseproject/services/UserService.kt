package com.ehb.dev4courseproject.services

import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.ehb.dev4courseproject.models.User
import com.ehb.dev4courseproject.repositories.UserRepository
import com.ehb.dev4courseproject.dto.CreateUserRequest
import com.ehb.dev4courseproject.dto.LoginUserRequest
import com.ehb.dev4courseproject.repositories.ItemRepository
import com.ehb.dev4courseproject.repositories.LoanRepository
import org.springframework.transaction.annotation.Transactional

@Service
class UserService {

    @Autowired
    lateinit var loanRepository: LoanRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var itemRepository: ItemRepository


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

    @Transactional
    fun deleteUser(userId: Long): User {
        val user = userRepository.findById(userId)
        if (user.isPresent) {
            loanRepository.deleteByUserId(userId)
            userRepository.deleteById(userId)
            return user.get()
        } else {
            throw EntityNotFoundException("User with id $userId not found")
        }
    }

    // User Login
    fun loginUser(user: LoginUserRequest): String? {
        val u = userRepository.findByUsername(user.username)
        if(u != null) {
            if(user.password == u.password) {
                u.accessToken = java.util.UUID.randomUUID().toString()
                //Setting expiration date to 1 day
                //(Millis * seconds * minutes * hours * days)
                u.expirationDate =  System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 1)
                userRepository.save(u)
                return u.accessToken
            } else {
                return null
            }
        } else {
            return null
        }
    }

    fun isAuthenticated(token: String): Boolean {
        val u = userRepository.findByAccessToken(token)
        if(u != null) {
            return u.expirationDate > System.currentTimeMillis()
        } else {
            return false
        }
    }

    fun getUserByToken(token: String): User? {
        return userRepository.findByAccessToken(token)
    }

}