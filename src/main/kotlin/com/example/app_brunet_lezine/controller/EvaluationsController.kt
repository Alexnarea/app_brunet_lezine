package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.dto.EvaluationsDto
import com.example.app_brunet_lezine.service.EvaluationsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/evaluations")
class EvaluationsController {

    @Autowired
    lateinit var evaluationsService: EvaluationsService

    @GetMapping
    fun findAll(): ResponseEntity<List<EvaluationsDto>> {
        val evaluations = evaluationsService.findAll()
        return ResponseEntity.ok(evaluations)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<EvaluationsDto> {
        val evaluation = evaluationsService.findById(id)
        return ResponseEntity.ok(evaluation)
    }

    @PostMapping
    fun save(@RequestBody dto: EvaluationsDto): ResponseEntity<EvaluationsDto> {
        val saved = evaluationsService.save(dto)
        return ResponseEntity.ok(saved)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: EvaluationsDto): ResponseEntity<EvaluationsDto> {
        val updated = evaluationsService.update(id, dto)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        evaluationsService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
