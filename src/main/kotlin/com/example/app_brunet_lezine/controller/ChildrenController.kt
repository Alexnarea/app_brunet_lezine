package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.dto.ChildrenDto
import com.example.app_brunet_lezine.response.SuccessResponse
import com.example.app_brunet_lezine.service.ChildrenService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/children")
class ChildrenController {
    @Autowired
    lateinit var childrenService: ChildrenService


    @GetMapping
    fun findAllChildren(): ResponseEntity<*> {
        val response = childrenService.findAll()
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<*> {
        val response = childrenService.findById(id)
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.OK)
    }

    @PostMapping
    fun save (@RequestBody @Valid childrenDto: ChildrenDto): ResponseEntity<Any> {
        val response = childrenService.save(childrenDto)
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid childrenDto: ChildrenDto): ResponseEntity<Any> {
        val response = childrenService.update(id, childrenDto)
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        val response = childrenService.delete(id)
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.OK)
    }
}