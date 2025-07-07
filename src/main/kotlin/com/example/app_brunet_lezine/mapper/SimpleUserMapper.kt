package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.SimpleUserDto
import com.example.app_brunet_lezine.entity.UserEntity
import org.springframework.stereotype.Component

@Component
object SimpleUserMapper {
    fun toUserSimpleDto(user: UserEntity): SimpleUserDto {
        val simpleUserDto = SimpleUserDto()
        simpleUserDto.id = user.id
        simpleUserDto.username = user.username
        return simpleUserDto
    }
}