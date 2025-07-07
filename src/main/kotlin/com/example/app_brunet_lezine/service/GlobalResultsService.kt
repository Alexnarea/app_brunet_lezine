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
        updatedEntity.id = existing.id

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

        val responses = responsesRepository.findByEvaluationId(evaluationId)
        if (responses.isEmpty()) throw IllegalStateException("No hay respuestas para la evaluación $evaluationId")

        val groupedByAge = responses
            .filter { it.item?.referenceAgeMonths != null }
            .groupBy { it.item!!.referenceAgeMonths!! }
            .toSortedMap(reverseOrder())

        var edadBase: Double? = null
        var itemsAprobadosSumados = 0

        // Detectar base
        for ((age, group) in groupedByAge) {
            val passed = group.count { it.passed == true }
            val total = group.size

            if (passed == total && total > 0) {
                // Pasó todos los ítems → sube a la siguiente edad
                edadBase = age + 12.0
                itemsAprobadosSumados = 0
                break
            }

            if (passed >= 1) {
                // Posible base
                if (edadBase == null || age < edadBase) {
                    edadBase = age.toDouble()
                }
            }
        }

        if (edadBase == null) {
            edadBase = 0.0
        }

        // Sumar ítems aprobados de la edad base y superiores
        for ((age, group) in groupedByAge) {
            if (age >= edadBase) {
                val passed = group.count { it.passed == true }
                itemsAprobadosSumados += passed
            }
        }

        val edadDesarrolloMeses = edadBase + (itemsAprobadosSumados * 1.5)

        val edadCronologicaMeses = evaluation.chronologicalAgeMonths?.toDouble()
            ?: throw IllegalStateException("Edad cronológica no válida")

        val edadDesarrolloDias = edadDesarrolloMeses * 30.44
        val edadCronologicaDias = edadCronologicaMeses * 30.44

        val coeficiente = if (edadCronologicaDias > 0)
            (edadDesarrolloDias / edadCronologicaDias) * 100
        else 0.0

        val years = (edadDesarrolloMeses / 12).toInt()
        val months = (edadDesarrolloMeses % 12).toInt()
        val resultYears = "$years años y $months meses"

        val classification = when {
            coeficiente >= 90 -> "Desarrollo normal"
            coeficiente >= 75 -> "Retraso leve"
            coeficiente >= 50 -> "Retraso moderado"
            else -> "Retraso severo"
        }

        val existing = globalResultsRepository.findByEvaluationId(evaluationId).orElse(null)
        val result = existing ?: GlobalResults().apply { this.evaluation = evaluation }

        result.totalMonthsApproved = BigDecimal.valueOf(edadDesarrolloMeses).setScale(1, RoundingMode.HALF_UP)
        result.coefficient = BigDecimal.valueOf(coeficiente).setScale(2, RoundingMode.HALF_UP)
        result.resultYears = resultYears
        result.resultDetail = "Edad de desarrollo: $resultYears"
        result.classification = classification

        val saved = globalResultsRepository.save(result)
        return globalResultsMapper.toGlobalResultsDto(saved)
    }
}
