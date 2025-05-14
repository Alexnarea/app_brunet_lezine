package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.EvaluatorsDto
import com.example.app_brunet_lezine.entity.Evaluators
import com.example.app_brunet_lezine.mapper.EvaluatorsMapper
import com.example.app_brunet_lezine.repository.EvaluatorsRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EvaluatorsService {
    @Autowired
    lateinit var evaluatorsRepository: EvaluatorsRepository
    @Autowired
    lateinit var evaluatorsMapper: EvaluatorsMapper

    fun findAll(): List<EvaluatorsDto>{
        val evaluators = evaluatorsRepository.findAll()
        return evaluators.map { evaluatorsMapper.toEvaluatorsDto(it) }
    }

    fun findById(id: Long): EvaluatorsDto{
        val evaluators = evaluatorsRepository.findById(id)
            .orElseThrow{EntityNotFoundException("Evaluators with id $id not found")}
        return evaluatorsMapper.toEvaluatorsDto(evaluators)
    }

    fun save(evaluatorDto: EvaluatorsDto): EvaluatorsDto {
        val evaluators = evaluatorsMapper.toEntity(evaluatorDto)
        val saveEvaluators = evaluatorsRepository.save(evaluators)
        return evaluatorsMapper.toEvaluatorsDto(saveEvaluators)
    }

    fun update(id: Long, evaluatorDto: EvaluatorsDto): EvaluatorsDto {
        val evaluators = evaluatorsRepository.findById(id)
            .orElseThrow{EntityNotFoundException("Evaluators with id $id not found")}
        evaluators.apply {
            speciality = evaluatorDto.speciality
        }
        val updatedEvaluators = evaluatorsRepository.save(evaluators)
        return evaluatorsMapper.toEvaluatorsDto(updatedEvaluators)
    }

    fun delete(id: Long) {
        val evaluators = evaluatorsRepository.findById(id)
            .orElseThrow{EntityNotFoundException("Evaluators with id $id not found")}
        evaluatorsRepository.delete(evaluators)
    }
}