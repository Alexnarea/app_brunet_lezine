package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.TestItemsDto
import com.example.app_brunet_lezine.entity.TestItems
import com.example.app_brunet_lezine.mapper.TestItemsMapper
import com.example.app_brunet_lezine.repository.DomainsRepository
import com.example.app_brunet_lezine.repository.TestItemsRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TestItemsService {

    @Autowired
    lateinit var testItemsRepository: TestItemsRepository

    @Autowired
    lateinit var domainsRepository: DomainsRepository

    @Autowired
    lateinit var testItemsMapper: TestItemsMapper

    fun findAll(): List<TestItemsDto> {
        val items = testItemsRepository.findAll()
        return items.map { testItemsMapper.toTestDto(it) }
    }

    fun findById(id: Long): TestItemsDto {
        val testItem = testItemsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("TestItem con ID $id no encontrado") }
        return testItemsMapper.toTestDto(testItem)
    }

    @Transactional
    fun save(testItemsDto: TestItemsDto): TestItemsDto {
        val testItem = testItemsMapper.toEntity(testItemsDto)

        val domainId = testItemsDto.domainId
            ?: throw IllegalArgumentException("domainId es requerido")

        val domain = domainsRepository.findById(domainId)
            .orElseThrow { EntityNotFoundException("Domain con ID $domainId no encontrado") }

        testItem.domain = domain

        val savedItem = testItemsRepository.save(testItem)
        return testItemsMapper.toTestDto(savedItem)
    }

    @Transactional
    fun update(id: Long, testItemsDto: TestItemsDto): TestItemsDto {
        val existingItem = testItemsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("TestItem con ID $id no encontrado") }

        existingItem.apply {
            description = testItemsDto.description ?: description
            referenceAgeMonths = testItemsDto.referenceAgeMonths ?: referenceAgeMonths
            itemOrder = testItemsDto.itemOrder ?: itemOrder
        }

        if (testItemsDto.domainId != null) {
            val domain = domainsRepository.findById(testItemsDto.domainId!!)
                .orElseThrow { EntityNotFoundException("Domain con ID ${testItemsDto.domainId} no encontrado") }
            existingItem.domain = domain
        }

        val updatedItem = testItemsRepository.save(existingItem)
        return testItemsMapper.toTestDto(updatedItem)
    }

    @Transactional
    fun delete(id: Long) {
        val testItem = testItemsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("TestItem con ID $id no encontrado") }
        testItemsRepository.delete(testItem)
    }
}
