package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.ChildrenDto
import com.example.app_brunet_lezine.entity.Children
import org.springframework.stereotype.Component

@Component
object ChildrenMapper {

    fun toEntity(childrenDto: ChildrenDto): Children {
        val children = Children()
        children.id = childrenDto.id
        children.fullName = childrenDto.fullName
        children.nui = childrenDto.nui
        children.birthdate = childrenDto.birthdate
        children.gender = childrenDto.gender
        children.creationDate = childrenDto.creationDate
        return children
    }

    fun toChildrenDto(children: Children): ChildrenDto{
        val childrenDto = ChildrenDto()
        childrenDto.id = children.id
        childrenDto.fullName = children.fullName
        childrenDto.nui = children.nui
        childrenDto.birthdate = children.birthdate
        childrenDto.gender = children.gender
        childrenDto.creationDate = children.creationDate
        return childrenDto
    }
}