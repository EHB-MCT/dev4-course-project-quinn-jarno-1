package com.ehb.dev4courseproject.services

import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.ehb.dev4courseproject.repositories.ItemRepository
import com.ehb.dev4courseproject.models.Item
import com.ehb.dev4courseproject.dto.CreateItemRequest

@Service
class ItemService {

    @Autowired
    lateinit var itemRepository: ItemRepository

    fun getAllItems(): List<Item> {
        return itemRepository.findAll()
    }

    fun addItem(item: CreateItemRequest): Item {
        val item = Item(name = item.name, description = item.description, isLoanedOut = item.isLoanedOut);
        return itemRepository.save(item)
    }

}