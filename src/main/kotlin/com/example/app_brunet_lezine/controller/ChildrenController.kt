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
    fun findAllChildren(): ResponseEntity<SuccessResponse> {
        val children = childrenService.findAll()
        return ResponseEntity.ok(SuccessResponse(data = children, message = "Niño eliminado exitosamente"))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<SuccessResponse> {
        val child = childrenService.findById(id)
        return ResponseEntity.ok(SuccessResponse(data = child, message = "Niño eliminado exitosamente"))
    }

    @PostMapping
    fun save(@RequestBody @Valid childrenDto: ChildrenDto): ResponseEntity<SuccessResponse> {
        val saved = childrenService.save(childrenDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse(
            data = saved,
            message = "Niño eliminado exitosamente"
        ))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid childrenDto: ChildrenDto): ResponseEntity<SuccessResponse> {
        val updated = childrenService.update(id, childrenDto)
        return ResponseEntity.ok(SuccessResponse(data = updated, message = "Niño eliminado exitosamente"))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<SuccessResponse> {
        childrenService.delete(id)
        return ResponseEntity.ok(SuccessResponse(message = "Niño eliminado exitosamente"))
    }
}
