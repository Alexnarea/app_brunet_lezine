package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.ResponsesDto
import com.example.app_brunet_lezine.entity.Responses
import com.example.app_brunet_lezine.mapper.ResponsesMapper
import com.example.app_brunet_lezine.repository.EvaluationsRepository
import com.example.app_brunet_lezine.repository.ResponsesRepository
import com.example.app_brunet_lezine.repository.TestItemsRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ResponsesService {

    @Autowired
    lateinit var responsesRepository: ResponsesRepository

    @Autowired
    lateinit var responsesMapper: ResponsesMapper

    @Autowired
    lateinit var evaluationsRepository: EvaluationsRepository

    @Autowired
    lateinit var testItemsRepository: TestItemsRepository

    fun findAll(): List<ResponsesDto> {
        val responses = responsesRepository.findAll()
        return responses.map { responsesMapper.toResponsesDto(it) }
    }

    fun findById(id: Long): ResponsesDto {
        val response = responsesRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Response with id $id not found") }
        return responsesMapper.toResponsesDto(response)
    }

    fun findByEvaluationId(evaluationId: Long): List<ResponsesDto> {
        val responses = responsesRepository.findByEvaluationId(evaluationId)
        return responses.map { responsesMapper.toResponsesDto(it) }
    }


    fun save(dto: ResponsesDto): ResponsesDto {
        val evaluation = evaluationsRepository.findById(dto.evaluationId ?: throw IllegalArgumentException("Evaluation ID required"))
            .orElseThrow { EntityNotFoundException("Evaluation not found") }

        val item = testItemsRepository.findById(dto.itemId ?: throw IllegalArgumentException("Item ID required"))
            .orElseThrow { EntityNotFoundException("Test item not found") }

        val entity = responsesMapper.toEntity(dto, evaluation, item)
        val saved = responsesRepository.save(entity)
        return responsesMapper.toResponsesDto(saved)
    }

    @Transactional
    fun saveAll(responseList: List<Responses>) {
        responsesRepository.saveAll(responseList)
    }


    fun update(id: Long, dto: ResponsesDto): ResponsesDto {
        val existing = responsesRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Response with id $id not found") }

        val evaluation = evaluationsRepository.findById(dto.evaluationId ?: throw IllegalArgumentException("Evaluation ID required"))
            .orElseThrow { EntityNotFoundException("Evaluation not found") }

        val item = testItemsRepository.findById(dto.itemId ?: throw IllegalArgumentException("Item ID required"))
            .orElseThrow { EntityNotFoundException("Test item not found") }

        existing.evaluation = evaluation
        existing.item = item
        existing.passed = dto.passed

        val updated = responsesRepository.save(existing)
        return responsesMapper.toResponsesDto(updated)
    }

    fun delete(id: Long) {
        val response = responsesRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Response with id $id not found") }
        responsesRepository.delete(response)
    }
}
