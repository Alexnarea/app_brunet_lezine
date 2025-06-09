package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.EvaluationsDto
import com.example.app_brunet_lezine.entity.Evaluations
import org.springframework.stereotype.Component

@Component
object EvaluationsMapper {

    fun toEntity(evaluationsDto: EvaluationsDto): Evaluations {
        val evaluations = Evaluations()
        evaluations.id = evaluationsDto.id
        evaluations.applicationDate = evaluationsDto.applicationDate
        evaluations.chronologicalAgeMonths = evaluationsDto.chronologicalAgeMonths
        return evaluations
    }

    fun toEvaluationsDto(evaluations: Evaluations): EvaluationsDto {
        val evaluationsDto = EvaluationsDto()
        evaluationsDto.id = evaluations.id
        evaluationsDto.applicationDate = evaluations.applicationDate
        evaluationsDto.chronologicalAgeMonths = evaluations.chronologicalAgeMonths
        evaluationsDto.childrenId = evaluations.children?.id
        evaluationsDto.evaluatorId = evaluations.evaluator?.id
        return evaluationsDto
    }
}
