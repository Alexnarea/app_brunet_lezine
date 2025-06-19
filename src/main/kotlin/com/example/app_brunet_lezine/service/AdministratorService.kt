package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.AdministratorDto
import com.example.app_brunet_lezine.entity.Administrator
import com.example.app_brunet_lezine.mapper.AdministratorMapper
import com.example.app_brunet_lezine.repository.AdministratorRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AdministratorService {

    @Autowired
    lateinit var administratorRepository: AdministratorRepository

    @Autowired
    lateinit var administratorMapper: AdministratorMapper

    fun findAll(): List<AdministratorDto> {
        val admins = administratorRepository.findAll()
        return admins.map { administratorMapper.toAdminDto(it) }
    }

    fun findById(id: Long): AdministratorDto {
        val admin = administratorRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Administrator with id $id not found") }
        return administratorMapper.toAdminDto(admin)
    }

    fun save(administratorDto: AdministratorDto): AdministratorDto {
        val admin = administratorMapper.toEntity(administratorDto)
        val savedAdmin = administratorRepository.save(admin)
        return administratorMapper.toAdminDto(savedAdmin)
    }

    fun update(id: Long, administratorDto: AdministratorDto): AdministratorDto {
        val admin = administratorRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Administrator with id $id not found") }

        admin.apply {
            fullName = administratorDto.fullName
            nui = administratorDto.nui
            phone = administratorDto.phone
            birthdate = administratorDto.birthdate
            gender = administratorDto.gender
        }

        val updatedAdmin = administratorRepository.save(admin)
        return administratorMapper.toAdminDto(updatedAdmin)
    }

    fun delete(id: Long) {
        val admin = administratorRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Administrator with id $id not found") }
        administratorRepository.delete(admin)
    }
}
