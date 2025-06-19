package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.dto.EvaluationDetailDto
import com.example.app_brunet_lezine.dto.EvaluationRequestDto
import com.example.app_brunet_lezine.dto.EvaluationResultDto
import com.example.app_brunet_lezine.dto.EvaluationsDto
import com.example.app_brunet_lezine.service.EvaluationsService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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

    @GetMapping("/{id}/detail")
    fun getDetail(@PathVariable id: Long): ResponseEntity<EvaluationDetailDto> {
        val detail = evaluationsService.getEvaluationDetail(id)
        return ResponseEntity.ok(detail)
    }

    @PostMapping
    fun save(@RequestBody dto: EvaluationsDto): ResponseEntity<EvaluationsDto> {
        val saved = evaluationsService.save(dto)
        return ResponseEntity.ok(saved)
    }

    @PostMapping("/create-with-responses")
    fun createWithResponses(@Valid @RequestBody request: EvaluationRequestDto): ResponseEntity<EvaluationResultDto> {
        println("Llamando a createWithResponses con: $request") // 👈 añade este log
        val result = evaluationsService.createEvaluationWithResponses(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
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
