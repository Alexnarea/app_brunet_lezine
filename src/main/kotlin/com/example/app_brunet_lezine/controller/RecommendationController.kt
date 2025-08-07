package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.dto.RecommendationDto
import com.example.app_brunet_lezine.response.SuccessResponse
import com.example.app_brunet_lezine.service.RecommendationService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recommendations")
class RecommendationController {

    @Autowired
    lateinit var recommendationService: RecommendationService

    @GetMapping("/by-result/{resultId}")
    fun findByResultId(@PathVariable resultId: Long): ResponseEntity<SuccessResponse> {
        val recommendations = recommendationService.findByResultId(resultId)
        return ResponseEntity.ok(SuccessResponse(data = recommendations, message = "Recomendaciones encontradas"))
    }

    @PostMapping
    fun save(@RequestBody @Valid recommendationDto: RecommendationDto): ResponseEntity<SuccessResponse> {
        val saved = recommendationService.save(recommendationDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            SuccessResponse(data = saved, message = "Recomendación guardada correctamente")
        )
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<SuccessResponse> {
        recommendationService.delete(id)
        return ResponseEntity.ok(SuccessResponse(message = "Recomendación eliminada"))
    }
}
