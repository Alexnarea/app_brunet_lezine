package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.ChildrenDto
import com.example.app_brunet_lezine.mapper.ChildrenMapper
import com.example.app_brunet_lezine.repository.ChildrenRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ChildrenService {

    @Autowired
    lateinit var childrenRepository: ChildrenRepository
    @Autowired
    lateinit var childrenMapper: ChildrenMapper

    fun findAll(): List<ChildrenDto> {
        val children = childrenRepository.findAll()
        return children.map {childrenMapper.toChildrenDto(it)}
    }

    fun findById(id: Long): ChildrenDto {
        val children = childrenRepository.findById(id)
            .orElseThrow {EntityNotFoundException("Children with ID $id not found")}
        return childrenMapper.toChildrenDto(children)
    }

    fun save(childrenDto: ChildrenDto): ChildrenDto {
        childrenDto.creationDate = LocalDateTime.now()
        val children = childrenMapper.toEntity(childrenDto)
        val saveChildren = childrenRepository.save(children)
        return childrenMapper.toChildrenDto(saveChildren)
    }

    fun update(id: Long, childrenDto: ChildrenDto): ChildrenDto {
        val children = childrenRepository.findById(id)
        .orElseThrow {EntityNotFoundException("Children with ID $id not found")}
        children.apply {
            fullName = childrenDto.fullName
            nui = childrenDto.nui
            birthdate = childrenDto.birthdate
            gender = childrenDto.gender
        }
        val updateChildren = childrenRepository.save(children)
        return childrenMapper.toChildrenDto(updateChildren)
    }

    fun delete(id: Long) {
        val children = childrenRepository.findById(id)
            .orElseThrow{EntityNotFoundException("Children with ID $id not found")}
        childrenRepository.delete(children)
    }
}