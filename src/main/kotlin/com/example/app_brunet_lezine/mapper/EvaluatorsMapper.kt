package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.EvaluatorsDto
import com.example.app_brunet_lezine.entity.Evaluators
import org.springframework.stereotype.Component

@Component
object EvaluatorsMapper {
    fun toEntity(evaluatorsDto: EvaluatorsDto): Evaluators{
        val evaluators = Evaluators()
        evaluators.id = evaluatorsDto.id
        evaluators.speciality = evaluatorsDto.speciality
        return evaluators
    }

    fun toEvaluatorsDto(evaluators: Evaluators): EvaluatorsDto {
        val evaluatorsDto = EvaluatorsDto()
        evaluatorsDto.id = evaluators.id
        evaluatorsDto.speciality = evaluators.speciality
        return evaluatorsDto
    }
}