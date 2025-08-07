package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.RecommendationDto
import com.example.app_brunet_lezine.entity.GlobalResults
import com.example.app_brunet_lezine.entity.Recommendation
import org.springframework.stereotype.Component

@Component
object RecommendationMapper {

    fun toEntity(dto: RecommendationDto): Recommendation {
        val recommendation = Recommendation()
        recommendation.id = dto.id
        recommendation.activity = dto.activity
        recommendation.frequency = dto.frequency

        val globalResults = GlobalResults()
        globalResults.id = dto.resultId
        recommendation.globalResult = globalResults

        return recommendation
    }

    fun toRecommendationDto(entity: Recommendation): RecommendationDto {
        return RecommendationDto(
            id = entity.id,
            resultId = entity.globalResult?.id ?: throw IllegalStateException("GlobalResult is null"),
            activity = entity.activity ?: "",
            frequency = entity.frequency
        )
    }
}
