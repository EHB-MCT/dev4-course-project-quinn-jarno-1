package com.ehb.dev4courseproject.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import com.ehb.dev4courseproject.services.ItemService
import com.ehb.dev4courseproject.models.Item
import com.ehb.dev4courseproject.dto.CreateItemRequest

@RestController
@RequestMapping("items")
class ItemController {

    @Autowired
    lateinit var itemService: ItemService;

    @GetMapping
    fun index(): List<Item> {
        val items = itemService.getAllItems();
        return items;
    }

    @PostMapping
    fun addItem(@RequestBody itemRequest: CreateItemRequest): Item {
        return itemService.addItem(itemRequest)
    }

}