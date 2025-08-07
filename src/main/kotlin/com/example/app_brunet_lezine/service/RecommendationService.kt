package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.RecommendationDto
import com.example.app_brunet_lezine.mapper.RecommendationMapper
import com.example.app_brunet_lezine.repository.RecommendationRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RecommendationService {

    @Autowired
    lateinit var recommendationRepository: RecommendationRepository

    @Autowired
    lateinit var recommendationMapper: RecommendationMapper

    fun findByResultId(resultId: Long): List<RecommendationDto> {
        val recommendations = recommendationRepository.findByGlobalResultId(resultId)
        return recommendations.map { recommendationMapper.toRecommendationDto(it) }
    }

    fun save(dto: RecommendationDto): RecommendationDto {
        val entity = recommendationMapper.toEntity(dto)
        val saved = recommendationRepository.save(entity)
        return recommendationMapper.toRecommendationDto(saved)
    }

    fun delete(id: Long) {
        val recommendation = recommendationRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Recommendation with ID $id not found") }
        recommendationRepository.delete(recommendation)
    }
}
