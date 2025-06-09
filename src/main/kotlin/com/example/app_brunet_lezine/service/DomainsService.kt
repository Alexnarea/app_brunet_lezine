package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.DomainsDto
import com.example.app_brunet_lezine.entity.Domains
import com.example.app_brunet_lezine.mapper.DomainsMapper
import com.example.app_brunet_lezine.repository.DomainsRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DomainsService {
    @Autowired
    lateinit var domainsRepository: DomainsRepository
    @Autowired
    lateinit var domainsMapper: DomainsMapper

    fun findAll(): List<DomainsDto> {
        val domains = domainsRepository.findAll()
        return domains.map {domainsMapper.toDomainsDto(it)}
    }

    fun findById(id: Long): DomainsDto {
        val domains = domainsRepository.findById(id)
            .orElseThrow{EntityNotFoundException("Domain with id $id not found")}
        return domainsMapper.toDomainsDto(domains)
    }

    fun save(domainsDto: DomainsDto): DomainsDto {
        val domains = domainsMapper.toEntity(domainsDto)
        val saveDomain = domainsRepository.save(domains)
        return domainsMapper.toDomainsDto(saveDomain)
    }

    fun update(id: Long, domainsDto: DomainsDto): DomainsDto {
        val domains = domainsRepository.findById(id)
        .orElseThrow{EntityNotFoundException("Domain with id $id not found")}
        domainsDto.apply {
            descriptionDomain = domainsDto.descriptionDomain
        }
        val updateDomains = domainsRepository.save(domains)
        return domainsMapper.toDomainsDto(updateDomains)
    }

    fun delete(id: Long) {
        val domains = domainsRepository.findById(id)
            .orElseThrow{EntityNotFoundException("Domain with id $id not found")}
        domainsRepository.deleteById(id)
    }
}