package com.ehb.dev4courseproject.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import com.ehb.dev4courseproject.services.ItemService
import com.ehb.dev4courseproject.models.Item
import com.ehb.dev4courseproject.dto.CreateItemRequest
import com.ehb.dev4courseproject.dto.CreateUserRequest
import com.ehb.dev4courseproject.models.User

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("items")
class ItemController {

    @Autowired
    lateinit var itemService: ItemService;

    // Get All Items
    @GetMapping
    fun getItems(): List<Item> {
        val items = itemService.getAllItems();
        return items;
    }

    // Create A New Item
    @PostMapping
    fun addItem(@RequestBody itemRequest: CreateItemRequest): Item {
        return itemService.addItem(itemRequest)
    }

    // Update An Existing Item
    @PutMapping("update/{itemId}")
    fun updateItem(@PathVariable itemId: Long, @RequestBody itemRequest: CreateItemRequest): Item {
        return itemService.updateItem(itemId, itemRequest)
    }

    // Delete An Item
    @DeleteMapping("/{itemId}")
    fun deleteItem(@PathVariable itemId: Long): Item {
        return itemService.deleteItem(itemId)
    }

}