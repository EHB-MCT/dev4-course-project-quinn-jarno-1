package com.ehb.dev4courseproject.repositories

import com.ehb.dev4courseproject.models.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface ItemRepository : JpaRepository<Item, Long> {
}