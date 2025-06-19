package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.GlobalResultsDto
import com.example.app_brunet_lezine.entity.GlobalResults
import com.example.app_brunet_lezine.mapper.GlobalResultsMapper
import com.example.app_brunet_lezine.repository.EvaluationsRepository
import com.example.app_brunet_lezine.repository.GlobalResultsRepository
import com.example.app_brunet_lezine.repository.ResponsesRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class GlobalResultsService {

    @Autowired
    lateinit var globalResultsRepository: GlobalResultsRepository

    @Autowired
    lateinit var evaluationsRepository: EvaluationsRepository

    @Autowired
    lateinit var responsesRepository: ResponsesRepository

    @Autowired
    lateinit var globalResultsMapper: GlobalResultsMapper

    fun findAll(): List<GlobalResultsDto> {
        val results = globalResultsRepository.findAll()
        return results.map { globalResultsMapper.toGlobalResultsDto(it) }
    }

    fun findById(id: Long): GlobalResultsDto {
        val result = globalResultsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Global result with id $id not found") }
        return globalResultsMapper.toGlobalResultsDto(result)
    }

    fun findByEvaluationId(evaluationId: Long): GlobalResultsDto {
        val result = globalResultsRepository.findByEvaluationId(evaluationId)
            .orElseThrow { EntityNotFoundException("No hay resultado para evaluación $evaluationId") }
        return globalResultsMapper.toGlobalResultsDto(result)
    }

    fun save(dto: GlobalResultsDto): GlobalResultsDto {
        val evaluation = evaluationsRepository.findById(
            dto.evaluationId
                ?: throw IllegalArgumentException("Evaluation ID required")
        )
            .orElseThrow { EntityNotFoundException("Evaluation not found") }

        val entity = globalResultsMapper.toEntity(dto, evaluation)
        val saved = globalResultsRepository.save(entity)
        return globalResultsMapper.toGlobalResultsDto(saved)
    }

    fun update(id: Long, dto: GlobalResultsDto): GlobalResultsDto {
        val existing = globalResultsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Global result with id $id not found") }

        val evaluation = evaluationsRepository.findById(
            dto.evaluationId
                ?: throw IllegalArgumentException("Evaluation ID required")
        )
            .orElseThrow { EntityNotFoundException("Evaluation not found") }

        val updatedEntity = globalResultsMapper.toEntity(dto, evaluation)
        updatedEntity.id = existing.id // mantener el mismo ID

        val saved = globalResultsRepository.save(updatedEntity)
        return globalResultsMapper.toGlobalResultsDto(saved)
    }

    fun delete(id: Long) {
        val existing = globalResultsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Global result with id $id not found") }
        globalResultsRepository.delete(existing)
    }

    fun calculateAndSaveResult(evaluationId: Long): GlobalResultsDto {
        val evaluation = evaluationsRepository.findById(evaluationId)
            .orElseThrow { EntityNotFoundException("Evaluation with id $evaluationId not found") }

        val responses = responsesRepository.findAll()
            .filter { it.evaluation?.id == evaluationId && it.passed == true }

        val totalMonthsApproved = BigDecimal.valueOf(responses.size.toDouble() * 1.5)

        val chronologicalAge = evaluation.chronologicalAgeMonths
            ?: throw IllegalStateException("Invalid chronological age")

        val coefficient = totalMonthsApproved
            .divide(BigDecimal.valueOf(chronologicalAge.toDouble()), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))

        val years = totalMonthsApproved.divide(BigDecimal.valueOf(12), 0, RoundingMode.DOWN).toInt()
        val months = totalMonthsApproved.remainder(BigDecimal.valueOf(12)).toInt()
        val resultYears = "$years años y $months meses"

        val classification = when {
            coefficient >= BigDecimal.valueOf(90) -> "Desarrollo normal"
            coefficient >= BigDecimal.valueOf(75) -> "Retraso leve"
            coefficient >= BigDecimal.valueOf(50) -> "Retraso moderado"
            else -> "Retraso severo"
        }

        val existingOptional = globalResultsRepository.findByEvaluationId(evaluationId)
        val existingResult = if (existingOptional.isPresent) existingOptional.get() else null

        val result = existingResult ?: GlobalResults().apply {
            this.evaluation = evaluation
        }

        result.totalMonthsApproved = totalMonthsApproved
        result.coefficient = coefficient
        result.resultYears = resultYears
        result.resultDetail = "Edad de desarrollo estimada en $resultYears"
        result.classification = classification

        val savedResult = globalResultsRepository.save(result)
        return globalResultsMapper.toGlobalResultsDto(savedResult)
    }
}
