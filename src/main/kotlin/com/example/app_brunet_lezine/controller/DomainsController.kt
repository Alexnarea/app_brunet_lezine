package com.example.app_brunet_lezine.controller
import com.example.app_brunet_lezine.dto.DomainsDto
import com.example.app_brunet_lezine.response.SuccessResponse
import com.example.app_brunet_lezine.service.DomainsService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
@RestController
@RequestMapping("/domains")
class DomainsController {

    @Autowired
    lateinit var domainsService: DomainsService

    @GetMapping
    fun findAllDomains(): ResponseEntity<*> {
        val response = domainsService.findAll()
        return ResponseEntity(SuccessResponse(data = response, message = "Dominios obtenidos exitosamente"), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun findDomainById(@PathVariable id: Long): ResponseEntity<*> {
        val response = domainsService.findById(id)
        return ResponseEntity(SuccessResponse(data = response, message = "Dominio encontrado exitosamente"), HttpStatus.OK)
    }

    @PostMapping
    fun save(@RequestBody @Valid domainsDto: DomainsDto): ResponseEntity<Any> {
        val response = domainsService.save(domainsDto)
        return ResponseEntity(SuccessResponse(data = response, message = "Dominio creado exitosamente"), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid domainsDto: DomainsDto): ResponseEntity<Any> {
        val response = domainsService.update(id, domainsDto)
        return ResponseEntity(SuccessResponse(data = response, message = "Dominio actualizado exitosamente"), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Any> {
        val response = domainsService.delete(id)
        return ResponseEntity(SuccessResponse(data = response, message = "Dominio eliminado exitosamente"), HttpStatus.OK)
    }
}
