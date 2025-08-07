package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.*
import com.example.app_brunet_lezine.entity.Children
import com.example.app_brunet_lezine.entity.Evaluators
import com.example.app_brunet_lezine.mapper.EvaluationsMapper
import com.example.app_brunet_lezine.repository.*
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.Period
import java.time.LocalDate
import java.math.BigDecimal
import org.springframework.security.core.context.SecurityContextHolder

@Service
class EvaluationsService {

    @Autowired
    private lateinit var childrenRepository: ChildrenRepository

    @Autowired
    private lateinit var userRepository: UserRepository

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

    @Autowired
    lateinit var reportService: ReportService

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
                throw EntityNotFoundException("No hay respuestas asociadas a la evaluación con ID $id") }

            val items = responses.map { response ->
                val item = response.item
                    ?: throw EntityNotFoundException("Item no encontrado para respuesta con ID ${response.id}")
                EvaluationItemDto(
                    id = item.id ?: throw EntityNotFoundException("ID del Item no encontrado"),
                    task = item.description ?: "[Descripción no disponible]",
                    domain = item.domain?.descriptionDomain ?: "[Dominio no disponible]",
                    completed = response.passed ?: false,
                    referenceAgeMonths = item.referenceAgeMonths ?: 0
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

    fun getAdminDashboardData(): AdminDashboardDto {
        // Agrupar evaluaciones por evaluador y contar niños únicos evaluados por cada uno
        val evaluatorSummaries = evaluationsRepository.findAll()
            .groupBy { it.evaluator?.fullName ?: "[Sin nombre]" }
            .map { (name, evaluations) ->
                val uniqueChildren = evaluations.mapNotNull { it.children?.id }.toSet()
                EvaluatorSummaryDto(
                    evaluatorName = name,
                    childrenCount = uniqueChildren.size
                )
            }

        // Obtener niños con retraso leve
        val childrenWithDelay = evaluationsRepository
            .findLatestEvaluationsWithClassification("Retraso leve")
            .map {
                val child = it.children
                val birthdate = child?.birthdate
                val age = birthdate?.let { b -> Period.between(b, LocalDate.now()).years } ?: 0
                ChildWithDelayDto(
                    childName = child?.fullName ?: "[Sin nombre]",
                    age = age,
                    lastEvaluationDate = it.applicationDate?.toLocalDate() ?: LocalDate.MIN,
                    coefficient = it.globalResults?.coefficient ?: BigDecimal.ZERO,
                    evaluatorName = it.evaluator?.fullName ?: "[Desconocido]"
                )
            }

        // Obtener niños con retraso grave
        val childrenWithSevereDelay = evaluationsRepository
            .findLatestEvaluationsWithClassification("Retraso severo")
            .map {
                val child = it.children
                val birthdate = child?.birthdate
                val age = birthdate?.let { b -> Period.between(b, LocalDate.now()).years } ?: 0
                ChildWithDelayDto(
                    childName = child?.fullName ?: "[Sin nombre]",
                    age = age,
                    lastEvaluationDate = it.applicationDate?.toLocalDate() ?: LocalDate.MIN,
                    coefficient = it.globalResults?.coefficient ?: BigDecimal.ZERO,
                    evaluatorName = it.evaluator?.fullName ?: "[Desconocido]"
                )
            }

        // Contar usuarios, evaluadores y niños
        val totalUsers = userRepository.count().toInt()
        val totalEvaluators = evaluatorsRepository.count().toInt()
        val totalChildren = childrenRepository.count().toInt()

        return AdminDashboardDto(
            evaluators = evaluatorSummaries,
            childrenWithDelay = childrenWithDelay,
            childrenWithSevereDelay = childrenWithSevereDelay,
            totalUsers = totalUsers,
            totalEvaluators = totalEvaluators,
            totalChildren = totalChildren
        )
    }

    fun getEvaluatorDashboardDataByUsername(username: String): EvaluatorDashboardDto {
        val evaluator = evaluatorsRepository.findByUserUsername(username)
            ?: throw IllegalStateException("Evaluador no encontrado para el usuario: $username")

        val evaluations = evaluationsRepository.findByEvaluatorId(evaluator.id!!)
        val totalChildren = childrenRepository.count().toInt()

        val childrenWithDelay = evaluationsRepository
            .findLatestEvaluationsByEvaluatorWithClassification(evaluator.id!!, "Retraso leve")
            .map {
                val birthdate = it.children?.birthdate
                val age = birthdate?.let { b -> Period.between(b, LocalDate.now()).years } ?: 0
                ChildWithDelayDto(
                    childName = it.children?.fullName ?: "[Sin nombre]",
                    age = age,
                    lastEvaluationDate = it.applicationDate?.toLocalDate() ?: LocalDate.MIN,
                    coefficient = it.globalResults?.coefficient ?: BigDecimal.ZERO,
                    evaluatorName = it.evaluator?.fullName ?: "[Desconocido]"
                )
            }

        val childrenWithSevereDelay = evaluationsRepository
            .findLatestEvaluationsByEvaluatorWithClassification(evaluator.id!!, "Retraso severo") // ✅
            .map {
                val birthdate = it.children?.birthdate
                val age = birthdate?.let { b -> Period.between(b, LocalDate.now()).years } ?: 0
                ChildWithDelayDto(
                    childName = it.children?.fullName ?: "[Sin nombre]",
                    age = age,
                    lastEvaluationDate = it.applicationDate?.toLocalDate() ?: LocalDate.MIN,
                    coefficient = it.globalResults?.coefficient ?: BigDecimal.ZERO,
                    evaluatorName = it.evaluator?.fullName ?: "[Desconocido]"
                )
            }

        return EvaluatorDashboardDto(
            totalChildren = totalChildren,
            childrenWithDelay = childrenWithDelay,
            childrenWithSevereDelay = childrenWithSevereDelay // ✅
        )
    }


    @Transactional
    fun save(dto: EvaluationsDto): EvaluationsDto {
        val evaluation = evaluationsMapper.toEntity(dto)
        evaluation.children = dto.childrenId?.let { Children().apply { id = it } }
        evaluation.evaluator = dto.evaluatorId?.let { Evaluators().apply { id = it } }
        evaluation.applicationDate = dto.applicationDate ?: LocalDateTime.now()
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
        val username = SecurityContextHolder.getContext().authentication.name
        val evaluator = evaluatorsRepository.findByUserUsername(username)
            ?: throw IllegalStateException("No se encontró un evaluador asociado al usuario: $username")

        val evaluationsDto = EvaluationsDto().apply {
            childrenId = request.childrenId
            evaluatorId = evaluator.id
            chronologicalAgeMonths = request.chronologicalAgeMonths
            applicationDate = LocalDateTime.now()
        }

        val savedEvaluation = save(evaluationsDto)

        request.responses.forEach { resp ->
            val responsesDto = ResponsesDto().apply {
                evaluationId = savedEvaluation.id!!
                itemId = resp.itemId
                passed = resp.passed
            }
            responsesService.save(responsesDto)
        }

        // ⚠️ Solo llamas a saveByEvaluationId, que ya calcula los resultados y genera el PDF
        reportService.saveByEvaluationId(savedEvaluation.id!!)

        // ✅ Ya puedes usar el resultado actualizado
        val globalResult = globalResultsService.findByEvaluationId(savedEvaluation.id!!)

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
