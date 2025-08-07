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
@RequestMapping("/api/administrators")
class AdministratorController {

    @Autowired
    lateinit var administratorService: AdministratorService

    @GetMapping
    fun findAll(): ResponseEntity<SuccessResponse> {
        val response = administratorService.findAll()
        return ResponseEntity.ok(
            SuccessResponse(data = response, message = "Administradores encontrados")
        )
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<SuccessResponse> {
        val response = administratorService.findById(id)
        return ResponseEntity.ok(
            SuccessResponse(data = response, message = "Administrador encontrado")
        )
    }

    @PostMapping
    fun save(@RequestBody @Valid administratorDto: AdministratorDto): ResponseEntity<SuccessResponse> {
        val response = administratorService.save(administratorDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            SuccessResponse(data = response, message = "Administrador creado correctamente")
        )
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid administratorDto: AdministratorDto): ResponseEntity<SuccessResponse> {
        val response = administratorService.update(id, administratorDto)
        return ResponseEntity.ok(
            SuccessResponse(data = response, message = "Administrador actualizado correctamente")
        )
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<SuccessResponse> {
        administratorService.delete(id)
        return ResponseEntity.ok(
            SuccessResponse(data = null, message = "Administrador eliminado correctamente")
        )
    }
}
