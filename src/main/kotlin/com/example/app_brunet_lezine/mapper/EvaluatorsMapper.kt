package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.EvaluatorsDto
import com.example.app_brunet_lezine.entity.Evaluators
import com.example.app_brunet_lezine.entity.UserEntity
import org.springframework.stereotype.Component

@Component
object EvaluatorsMapper {

    fun toEntity(evaluatorsDto: EvaluatorsDto, user: UserEntity): Evaluators {
        val evaluators = Evaluators()
        evaluators.id = evaluatorsDto.id
        evaluators.speciality = evaluatorsDto.speciality
        evaluators.fullName = evaluatorsDto.fullName
        evaluators.nui = evaluatorsDto.nui
        evaluators.phone = evaluatorsDto.phone
        evaluators.birthdate = evaluatorsDto.birthdate
        evaluators.gender = evaluatorsDto.gender
        evaluators.user = user  // ✅ Ahora user sí está definido
        return evaluators
    }

    fun toEvaluatorsDto(evaluators: Evaluators): EvaluatorsDto {
        val evaluatorsDto = EvaluatorsDto()
        evaluatorsDto.id = evaluators.id
        evaluatorsDto.speciality = evaluators.speciality
        evaluatorsDto.fullName = evaluators.fullName
        evaluatorsDto.nui = evaluators.nui
        evaluatorsDto.phone = evaluators.phone
        evaluatorsDto.birthdate = evaluators.birthdate
        evaluatorsDto.gender = evaluators.gender
        evaluatorsDto.userId = evaluators.user?.id
        return evaluatorsDto
    }
}
