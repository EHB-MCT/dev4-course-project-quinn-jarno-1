package com.ehb.dev4courseproject.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import com.ehb.dev4courseproject.models.User
import com.ehb.dev4courseproject.services.UserService
import com.ehb.dev4courseproject.dto.CreateUserRequest

@RestController
@RequestMapping("users")
class UserController {

    @Autowired
    lateinit var userService: UserService;

    @GetMapping
    fun getUsers(): List<User> {
        //Get all users
        val users = userService.getAllUsers();
        return users;
    }

    @PostMapping
    fun saveUser(@RequestBody userRequest: CreateUserRequest): User {
        return userService.createUser(userRequest)
    }
}