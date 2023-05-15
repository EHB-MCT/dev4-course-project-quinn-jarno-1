package com.ehb.dev4courseproject.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import com.ehb.dev4courseproject.models.User
import com.ehb.dev4courseproject.services.UserService
import com.ehb.dev4courseproject.dto.CreateUserRequest
import com.ehb.dev4courseproject.dto.LoginUserRequest

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("users")
class UserController {

    @Autowired
    lateinit var userService: UserService;

    // Gets All Users
    @GetMapping
    fun getUsers(): List<User> {
        return userService.getAllUsers();
    }

    // Gets One User By ID
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long): User {
        return userService.getUser(userId)
    }

    // Gets One User By ID
    @GetMapping("token/{token}")
    fun getUser(@PathVariable token: String): User? {
        return userService.getUserByToken(token)
    }

    // Create A New User
    @PostMapping
    fun saveUser(@RequestBody userRequest: CreateUserRequest): User {
        return userService.createUser(userRequest)
    }

    // Updates An Existing User
    @PutMapping("update/{userId}")
    fun updateUser(@PathVariable userId: Long, @RequestBody userRequest: CreateUserRequest): User {
        return userService.updateUser(userId, userRequest)
    }

    // Updates An Existing Users Role
    @PutMapping("updateRole/{userId}")
    fun updateUserRole(@PathVariable userId: Long, @RequestBody userRequest: CreateUserRequest): User {
        return userService.updateUserRole(userId, userRequest)
    }

    // Deletes A User
    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: Long): User {
       return userService.deleteUser(userId)
    }

    // Login User
    @RequestMapping("/login")
    @PostMapping
    fun loginUser(@RequestBody userRequest: LoginUserRequest): String? {
        return userService.loginUser(userRequest)
    }
}