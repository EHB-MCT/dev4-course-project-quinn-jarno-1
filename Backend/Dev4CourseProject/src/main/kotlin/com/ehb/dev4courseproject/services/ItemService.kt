package com.ehb.dev4courseproject.services

import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.ehb.dev4courseproject.repositories.ItemRepository
import com.ehb.dev4courseproject.models.Item
import com.ehb.dev4courseproject.dto.CreateItemRequest
import com.ehb.dev4courseproject.dto.CreateUserRequest
import com.ehb.dev4courseproject.models.User

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

    fun getItem(itemId: Long): Item {
        return itemRepository.findById(itemId)
            .orElseThrow { EntityNotFoundException("User not found with ID $itemId") }
    }

    fun updateItem(itemId: Long, itemRequest: CreateItemRequest): Item {
        val item = getItem(itemId)
        item.name = itemRequest.name
        item.description = itemRequest.description
        return itemRepository.save(item)
    }

    fun deleteItem(itemId: Long): Item {
        val item = itemRepository.findById(itemId)
        if (item.isPresent) {
            itemRepository.deleteById(itemId)
            return item.get()
        } else {
            throw EntityNotFoundException("User with id $itemId not found")
        }
    }

}