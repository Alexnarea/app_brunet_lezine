package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.LoginDto
import com.example.app_brunet_lezine.dto.UserDto
import com.example.app_brunet_lezine.entity.RoleEntity
import com.example.app_brunet_lezine.entity.UserEntity
import com.example.app_brunet_lezine.mapper.UserMapper
import com.example.app_brunet_lezine.repository.UserRepository
import com.example.app_brunet_lezine.response.SuccessResponse
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.SQLException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun list(): List<UserEntity> = userRepository.findAll()

    fun listById(id: Long?): UserEntity? =
        id?.let { userRepository.findById(it).orElse(null) }

    fun updatePassword(loginDto: LoginDto): UserEntity {
        val password = loginDto.password?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("La nueva contraseña no puede estar vacía")

        val user = userRepository.findByUsername(loginDto.username!!)
            ?: throw IllegalArgumentException("Usuario no encontrado")

        user.password = passwordEncoder.encode(password)
        return userRepository.save(user)
    }

    @Transactional
    fun delete(id: Long?): SuccessResponse {
        try {
            val userId = id ?: throw IllegalArgumentException("Id es null")

            if (!userRepository.existsById(userId)) {
                throw IllegalArgumentException("Id no existe")
            }

            userRepository.deleteById(userId)
            return SuccessResponse("success", message = "Niño eliminado exitosamente")
        } catch (e: DataIntegrityViolationException) {
            throw SQLException("Violación de integridad de datos")
        } catch (e: Exception) {
            throw SQLException(e.message)
        }
    }

    fun login(loginDto: LoginDto): UserEntity {
        val user = userRepository.findByUsername(loginDto.username!!)
            ?: throw BadCredentialsException("Usuario no encontrado")

        if (user.locked) throw LockedException("Usuario bloqueado")
        if (user.disabled) throw DisabledException("Usuario deshabilitado")

        if (!passwordEncoder.matches(loginDto.password, user.password)) {
            throw BadCredentialsException("Contraseña incorrecta")
        }

        return user
    }

    fun create(userDto: UserDto): UserEntity {
        require(!userDto.username.isNullOrBlank()) { "El nombre de usuario no puede estar vacío" }
        require(!userDto.email.isNullOrBlank()) { "El email no puede estar vacío" }
        require(!userDto.password.isNullOrBlank()) { "La contraseña no puede estar vacía" }

        if (userRepository.existsByUsername(userDto.username!!)) {
            throw IllegalArgumentException("El nombre de usuario ya existe")
        }

        if (userRepository.existsByEmail(userDto.email!!)) {
            throw IllegalArgumentException("El email ya está registrado")
        }

        // 🛠️ Aseguramos que se cree uno nuevo, no se reutilice uno existente
        userDto.id = null

        val user = UserMapper.toEntity(userDto)
        user.password = passwordEncoder.encode(userDto.password)

        return userRepository.save(user)
    }


    fun update(id: Long, userDto: UserDto): UserEntity {
        val user = userRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Usuario no encontrado") }

        user.username = userDto.username?.trim() ?: user.username
        user.email = userDto.email?.trim() ?: user.email
        user.locked = userDto.locked ?: user.locked
        user.disabled = userDto.disabled ?: user.disabled

        // ✅ Solo actualiza la contraseña si fue enviada
        if (!userDto.password.isNullOrBlank()) {
            user.password = passwordEncoder.encode(userDto.password)
        }

        // ✅ Solo actualiza el rol si ha cambiado
        val currentRole = user.roles.firstOrNull()?.role
        if (!userDto.role.isNullOrBlank() && userDto.role != currentRole) {
            user.roles.clear()
            val newRole = RoleEntity().apply {
                role = userDto.role
                this.user = user
            }
            user.roles.add(newRole)
        }

        return userRepository.save(user)
    }

}
