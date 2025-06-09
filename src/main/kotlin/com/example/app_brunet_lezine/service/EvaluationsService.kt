package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.EvaluationsDto
import com.example.app_brunet_lezine.entity.Children
import com.example.app_brunet_lezine.entity.Evaluations
import com.example.app_brunet_lezine.entity.Evaluators
import com.example.app_brunet_lezine.mapper.EvaluationsMapper
import com.example.app_brunet_lezine.repository.EvaluationsRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class EvaluationsService {

    @Autowired
    lateinit var evaluationsRepository: EvaluationsRepository

    @Autowired
    lateinit var evaluationsMapper: EvaluationsMapper

    fun findAll(): List<EvaluationsDto> {
        val evaluations = evaluationsRepository.findAll()
        return evaluations.map { evaluationsMapper.toEvaluationsDto(it) }
    }

    fun findById(id: Long): EvaluationsDto {
        val evaluation = evaluationsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Evaluation with ID $id not found") }
        return evaluationsMapper.toEvaluationsDto(evaluation)
    }

    @Transactional
    fun save(dto: EvaluationsDto): EvaluationsDto {
        val evaluation = evaluationsMapper.toEntity(dto)

        // Solo seteamos las relaciones por el id para evitar carga innecesaria
        evaluation.children = dto.childrenId?.let { Children().apply { id = it } }
        evaluation.evaluator = dto.evaluatorId?.let { Evaluators().apply { id = it } }

        // Si quieres establecer applicationDate al guardar, descomenta:
        // evaluation.applicationDate = LocalDateTime.now()

        val savedEvaluation = evaluationsRepository.save(evaluation)
        return evaluationsMapper.toEvaluationsDto(savedEvaluation)
    }

    @Transactional
    fun update(id: Long, dto: EvaluationsDto): EvaluationsDto {
        val evaluation = evaluationsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Evaluation with ID $id not found") }

        evaluation.applicationDate = dto.applicationDate
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
}
