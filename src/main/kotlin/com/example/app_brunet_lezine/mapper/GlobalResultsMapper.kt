package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.GlobalResultsDto
import com.example.app_brunet_lezine.entity.Evaluations
import com.example.app_brunet_lezine.entity.GlobalResults
import org.springframework.stereotype.Component

@Component
object GlobalResultsMapper {
    fun toEntity(globalResultsDto: GlobalResultsDto, evaluations: Evaluations): GlobalResults{
        val globalResults = GlobalResults()
        globalResults.id = globalResultsDto.id
        globalResults.totalMonthsApproved = globalResultsDto.totalMonthsApproved
        globalResults.coefficient = globalResultsDto.coefficient
        globalResults.resultYears = globalResultsDto.resultYears
        globalResults.resultDetail = globalResultsDto.resultDetail
        globalResults.classification = globalResultsDto.classification
        globalResults.evaluation = evaluations
        return globalResults
    }

    fun toGlobalResultsDto(globalResults: GlobalResults): GlobalResultsDto {
        val globalResultsDto = GlobalResultsDto()
        globalResultsDto.id = globalResults.id
        globalResultsDto.totalMonthsApproved = globalResults.totalMonthsApproved
        globalResultsDto.coefficient = globalResults.coefficient
        globalResultsDto.resultYears = globalResults.resultYears
        globalResultsDto.resultDetail = globalResults.resultDetail
        globalResultsDto.classification = globalResults.classification
        globalResultsDto.evaluationId = globalResults.evaluation?.id
        return globalResultsDto
    }
}