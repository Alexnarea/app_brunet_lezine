package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.dto.GlobalResultsDto
import com.example.app_brunet_lezine.service.GlobalResultsService
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/global-results")
class GlobalResultsController {

    @Autowired
    lateinit var globalResultsService: GlobalResultsService

    @GetMapping
    fun getAll(): ResponseEntity<List<GlobalResultsDto>> {
        val list = globalResultsService.findAll()
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<GlobalResultsDto> {
        return try {
            val result = globalResultsService.findById(id)
            ResponseEntity.ok(result)
        } catch (ex: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/evaluation/{evaluationId}")
    fun getByEvaluationId(@PathVariable evaluationId: Long): ResponseEntity<GlobalResultsDto> {
        return try {
            val result = globalResultsService.findByEvaluationId(evaluationId)
            ResponseEntity.ok(result)
        } catch (ex: EntityNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }


    @PostMapping
    fun create(@RequestBody dto: GlobalResultsDto): ResponseEntity<GlobalResultsDto> {
        return try {
            val created = globalResultsService.save(dto)
            ResponseEntity.status(HttpStatus.CREATED).body(created)
        } catch (ex: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: GlobalResultsDto): ResponseEntity<GlobalResultsDto> {
        return try {
            val updated = globalResultsService.update(id, dto)
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
            globalResultsService.delete(id)
            ResponseEntity.noContent().build()
        } catch (ex: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    // Endpoint para calcular y guardar resultado basado en evaluationId
    @PostMapping("/calculate/{evaluationId}")
    fun calculateAndSave(@PathVariable evaluationId: Long): ResponseEntity<GlobalResultsDto> {
        return try {
            val result = globalResultsService.calculateAndSaveResult(evaluationId)
            ResponseEntity.ok(result)
        } catch (ex: EntityNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        } catch (ex: IllegalStateException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }

}
