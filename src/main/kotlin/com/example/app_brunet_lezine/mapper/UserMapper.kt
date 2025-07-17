package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.UserDto
import com.example.app_brunet_lezine.entity.RoleEntity
import com.example.app_brunet_lezine.entity.UserEntity
import org.springframework.stereotype.Component

@Component
object UserMapper {

    fun toEntity(userDto: UserDto): UserEntity {
        val user = UserEntity()

        // Solo asignamos el ID si viene explícitamente (por ejemplo, en una actualización)
        userDto.id?.let {
            user.id = it
        }

        user.username = userDto.username
        user.email = userDto.email
        user.locked = userDto.locked ?: false
        user.disabled = userDto.disabled ?: false

        // ✅ Asignar rol si viene desde el DTO y no está duplicado
        userDto.role?.let { roleStr ->
            // Verificamos que el rol no esté ya en la lista (por si acaso)
            if (user.roles.none { it.role == roleStr }) {
                val roleEntity = RoleEntity().apply {
                    role = roleStr
                    this.user = user
                }
                user.roles = mutableListOf(roleEntity)
            }
        }

        return user
    }

    fun toUserDto(user: UserEntity): UserDto {
        return UserDto().apply {
            id = user.id
            username = user.username
            email = user.email
            locked = user.locked
            disabled = user.disabled
            role = user.roles.firstOrNull()?.role
        }
    }
}
