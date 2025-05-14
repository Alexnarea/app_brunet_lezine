package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.DomainsDto
import com.example.app_brunet_lezine.entity.Domains
import org.springframework.stereotype.Component

@Component
object DomainsMapper {
    fun toEntity(domainsDto: DomainsDto): Domains{
        val domains = Domains()
        domains.id = domainsDto.id
        domains.descriptionDomain = domainsDto.descriptionDomain
        return domains
    }

    fun toDomainsDto(domains: Domains): DomainsDto{
        val domainsDto = DomainsDto()
        domainsDto.id = domains.id
        domainsDto.descriptionDomain = domainsDto.descriptionDomain
        return domainsDto
    }
}