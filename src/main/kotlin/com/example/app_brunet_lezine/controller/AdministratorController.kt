package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.dto.AdministratorDto
import com.example.app_brunet_lezine.response.SuccessResponse
import com.example.app_brunet_lezine.service.AdministratorService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/administrators")
class AdministratorController {

    @Autowired
    lateinit var administratorService: AdministratorService

    @GetMapping
    fun findAll(): ResponseEntity<*> {
        val response = administratorService.findAll()
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<*> {
        val response = administratorService.findById(id)
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.OK)
    }

    @PostMapping
    fun save(@RequestBody @Valid administratorDto: AdministratorDto): ResponseEntity<Any> {
        val response = administratorService.save(administratorDto)
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid administratorDto: AdministratorDto): ResponseEntity<Any> {
        val response = administratorService.update(id, administratorDto)
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        administratorService.delete(id)
        return ResponseEntity(SuccessResponse(data = "Administrator with id $id deleted"), HttpStatus.OK)
    }
}
