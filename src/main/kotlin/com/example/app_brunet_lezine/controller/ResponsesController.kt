package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.dto.ResponsesDto
import com.example.app_brunet_lezine.service.ResponsesService
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/responses")
class ResponsesController {

    @Autowired
    lateinit var responsesService: ResponsesService

    @GetMapping
    fun getAll(): ResponseEntity<List<ResponsesDto>> {
        val list = responsesService.findAll()
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<ResponsesDto> {
        return try {
            val response = responsesService.findById(id)
            ResponseEntity.ok(response)
        } catch (ex: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/by-evaluation/{evaluationId}")
    fun getByEvaluationId(@PathVariable evaluationId: Long): ResponseEntity<List<ResponsesDto>> {
        val responses = responsesService.findByEvaluationId(evaluationId)
        return ResponseEntity.ok(responses)
    }

    @PostMapping
    fun create(@RequestBody dto: ResponsesDto): ResponseEntity<ResponsesDto> {
        return try {
            val created = responsesService.save(dto)
            ResponseEntity.status(HttpStatus.CREATED).body(created)
        } catch (ex: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: ResponsesDto): ResponseEntity<ResponsesDto> {
        return try {
            val updated = responsesService.update(id, dto)
            ResponseEntity.ok(updated)
        } catch (ex: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (ex: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            responsesService.delete(id)
            ResponseEntity.noContent().build()
        } catch (ex: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}
