package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.UserDto
import com.example.app_brunet_lezine.entity.UserEntity
import org.springframework.stereotype.Component

@Component
object UserMapper {

    fun toEntity(userDto: UserDto): UserEntity {
        val user = UserEntity()
        user.id = userDto.id
        user.username = userDto.username
        // user.password = userDto.password
        user.email = userDto.email
        user.locked = userDto.locked ?: false
        user.disabled = userDto.disabled ?: false
        return user
    }

    fun toUserDto(user: UserEntity): UserDto {
        val userDto = UserDto()
        userDto.id = user.id
        userDto.username = user.username
        // userDto.password = null
        userDto.email = user.email
        userDto.locked = user.locked
        userDto.disabled = user.disabled
        return userDto
    }
}