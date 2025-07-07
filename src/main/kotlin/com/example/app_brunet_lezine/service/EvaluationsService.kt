package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.*
import com.example.app_brunet_lezine.entity.Children
import com.example.app_brunet_lezine.entity.Evaluators
import com.example.app_brunet_lezine.mapper.EvaluationsMapper
import com.example.app_brunet_lezine.repository.EvaluationsRepository
import com.example.app_brunet_lezine.repository.EvaluatorsRepository
import com.example.app_brunet_lezine.repository.GlobalResultsRepository
import com.example.app_brunet_lezine.repository.ResponsesRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import org.springframework.security.core.context.SecurityContextHolder


@Service
class EvaluationsService {

    @Autowired
    lateinit var evaluationsRepository: EvaluationsRepository

    @Autowired
    lateinit var responsesRepository: ResponsesRepository

    @Autowired
    lateinit var globalResultsRepository: GlobalResultsRepository

    @Autowired
    lateinit var evaluationsMapper: EvaluationsMapper

    @Autowired
    lateinit var responsesService: ResponsesService

    @Autowired
    lateinit var testItemsService: TestItemsService

    @Autowired
    lateinit var globalResultsService: GlobalResultsService
    @Autowired
    lateinit var evaluatorsRepository: EvaluatorsRepository

    fun findAll(): List<EvaluationsDto> {
        val evaluations = evaluationsRepository.findAll()
        return evaluations.map { evaluationsMapper.toEvaluationsDto(it) }
    }

    fun findById(id: Long): EvaluationsDto {
        val evaluation = evaluationsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Evaluation with ID $id not found") }
        return evaluationsMapper.toEvaluationsDto(evaluation)
    }

    fun getEvaluationDetail(id: Long): EvaluationDetailDto {
        try {
            val evaluation = evaluationsRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Evaluación con ID $id no encontrada") }

            val globalResult = globalResultsRepository.findByEvaluationId(id)
                .orElseThrow { EntityNotFoundException("Resultado global no encontrado para evaluación $id") }

            val responses = responsesRepository.findByEvaluationId(id)
            if (responses.isEmpty()) {
                throw EntityNotFoundException("No hay respuestas asociadas a la evaluación con ID $id")
            }

            val items = responses.map { response ->
                val item = response.item
                    ?: throw EntityNotFoundException("Item no encontrado para respuesta con ID ${response.id}")
                EvaluationItemDto(
                    id = item.id ?: throw EntityNotFoundException("ID del Item no encontrado"),
                    task = item.description ?: "[Descripción no disponible]",
                    domain = item.domain?.descriptionDomain ?: "[Dominio no disponible]",
                    completed = response.passed ?: false,
                    referenceAgeMonths = item.referenceAgeMonths ?: 0 // ✅ Aquí va el nuevo dato
                )
        }

            return EvaluationDetailDto(
                id = evaluation.id ?: throw EntityNotFoundException("ID de evaluación no encontrado"),
                applicationDate = evaluation.applicationDate ?: throw EntityNotFoundException("Fecha no encontrada"),
                chronologicalAgeMonths = evaluation.chronologicalAgeMonths ?: 0,
                childrenId = evaluation.children?.id ?: throw EntityNotFoundException("ID de niño no encontrado"),
                evaluatorId = evaluation.evaluator?.id ?: throw EntityNotFoundException("ID de evaluador no encontrado"),
                resultYears = globalResult.resultYears ?: "[No calculado]",
                coefficient = globalResult.coefficient?.toDouble() ?: 0.0,
                classification = globalResult.classification ?: "[Sin clasificación]",
                observaciones = "",
                items = items
            )
        } catch (e: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error al obtener detalle de evaluación: ${e.message}"
            )
        }
    }

    @Transactional
    fun save(dto: EvaluationsDto): EvaluationsDto {
        val evaluation = evaluationsMapper.toEntity(dto)
        evaluation.children = dto.childrenId?.let { Children().apply { id = it } }
        evaluation.evaluator = dto.evaluatorId?.let { Evaluators().apply { id = it } }
        evaluation.applicationDate = dto.applicationDate ?: LocalDateTime.now() // ✅ siempre se asegura fecha
        val savedEvaluation = evaluationsRepository.save(evaluation)
        return evaluationsMapper.toEvaluationsDto(savedEvaluation)
    }

    @Transactional
    fun update(id: Long, dto: EvaluationsDto): EvaluationsDto {
        val evaluation = evaluationsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Evaluation with ID $id not found") }

        evaluation.applicationDate = dto.applicationDate ?: evaluation.applicationDate
        evaluation.chronologicalAgeMonths = dto.chronologicalAgeMonths
        evaluation.children = dto.childrenId?.let { Children().apply { this.id = it } }
        evaluation.evaluator = dto.evaluatorId?.let { Evaluators().apply { this.id = it } }

        val updatedEvaluation = evaluationsRepository.save(evaluation)
        return evaluationsMapper.toEvaluationsDto(updatedEvaluation)
    }

    @Transactional
    fun delete(id: Long) {
        val evaluation = evaluationsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Evaluation with ID $id not found") }
        evaluationsRepository.delete(evaluation)
    }

    @Transactional
    fun createEvaluationWithResponses(request: EvaluationRequestDto): EvaluationResultDto {
        println("➡️ Iniciando creación de evaluación con respuestas: $request")

        val username = SecurityContextHolder.getContext().authentication.name
        println("👤 Usuario autenticado: $username")

        // Busca el evaluador por el username
        val evaluator = evaluatorsRepository.findByUserUsername(username)
            ?: throw IllegalStateException("No se encontró un evaluador asociado al usuario: $username")

        val evaluationsDto = EvaluationsDto().apply {
            childrenId = request.childrenId
            evaluatorId = evaluator.id  // ✅ Aquí el ID correcto automáticamente
            chronologicalAgeMonths = request.chronologicalAgeMonths
            applicationDate = LocalDateTime.now()
        }

        val savedEvaluation = save(evaluationsDto)
        println("✅ Evaluación guardada con ID: ${savedEvaluation.id}")

        request.responses.forEach { resp ->
            println("📝 Guardando respuesta para itemId=${resp.itemId}, passed=${resp.passed}")
            val responsesDto = ResponsesDto().apply {
                evaluationId = savedEvaluation.id!!
                itemId = resp.itemId
                passed = resp.passed
            }
            responsesService.save(responsesDto)
        }

        println("🔄 Calculando resultado global para evaluación ID: ${savedEvaluation.id}")
        val globalResult = globalResultsService.calculateAndSaveResult(savedEvaluation.id!!)

        println("✅ Resultado global calculado: $globalResult")

        return EvaluationResultDto(
            evaluationId = savedEvaluation.id!!,
            totalMonthsApproved = globalResult.totalMonthsApproved?.toDouble() ?: 0.0,
            coefficient = globalResult.coefficient?.toDouble() ?: 0.0,
            classification = globalResult.classification ?: "",
            resultYears = globalResult.resultYears ?: "",
            resultDetail = globalResult.resultDetail ?: ""
        )
    }

}
