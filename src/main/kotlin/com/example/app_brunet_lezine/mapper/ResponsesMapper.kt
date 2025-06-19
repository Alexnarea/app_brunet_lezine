package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.ResponsesDto
import com.example.app_brunet_lezine.entity.Evaluations
import com.example.app_brunet_lezine.entity.Responses
import com.example.app_brunet_lezine.entity.TestItems
import org.springframework.stereotype.Component

@Component
object ResponsesMapper {

    fun toEntity(responsesDto: ResponsesDto, evaluations: Evaluations, testItems: TestItems): Responses {
        val responses = Responses()
        responses.id = responsesDto.id
        responses.evaluation = evaluations
        responses.item = testItems
        responses.passed = responsesDto.passed
        return responses
    }

    fun toResponsesDto(responses: Responses): ResponsesDto {
        val responsesDto = ResponsesDto()
        responsesDto.id = responses.id
        responsesDto.evaluationId = responses.evaluation?.id
        responsesDto.itemId = responses.item?.id
        responsesDto.passed = responses.passed
        return responsesDto
    }
}