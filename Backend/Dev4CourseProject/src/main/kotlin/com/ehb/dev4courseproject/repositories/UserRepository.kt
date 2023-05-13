package com.ehb.dev4courseproject.repositories

import com.ehb.dev4courseproject.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface UserRepository: JpaRepository<User,Long> {
}