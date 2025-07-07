package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.LoginDto
import com.example.app_brunet_lezine.dto.UserDto
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

    fun listById(id: Long?): UserEntity? = id?.let { userRepository.findById(it).orElse(null) }

    fun updatePassword(loginDto: LoginDto): UserEntity {
        if (loginDto.password.isNullOrBlank()) {
            throw Exception("La nueva contraseña no puede estar vacía")
        }

        val user = userRepository.findByUsername(loginDto.username!!)
            ?: throw Exception("Usuario no encontrado")

        user.password = passwordEncoder.encode(loginDto.password)
        return userRepository.save(user)
    }

    @Transactional
    fun delete(id: Long?): SuccessResponse {
        try {
            id?.let {
                if (!userRepository.existsById(it)) throw Exception("Id no existe")
                userRepository.deleteById(it)
            } ?: throw Exception("Id es null")
            return SuccessResponse("success")
        } catch (e: DataIntegrityViolationException) {
            throw SQLException("Violación de integridad de datos")
        } catch (e: Exception) {
            throw SQLException(e.message)
        }
    }

    fun login(loginDto: LoginDto): UserEntity {
        val user = userRepository.findByUsername(loginDto.username!!)
            ?: throw BadCredentialsException("Usuario no encontrado")

        if (user.locked == true) throw LockedException("Usuario bloqueado")
        if (user.disabled == true) throw DisabledException("Usuario deshabilitado")

        if (!passwordEncoder.matches(loginDto.password, user.password)) {
            throw BadCredentialsException("Contraseña incorrecta")
        }
        return user
    }


    fun create(userDto: UserDto): UserEntity {
        if (userDto.password.isNullOrBlank()) {
            throw IllegalArgumentException("La contraseña no puede estar vacía")
        }

        if (userDto.username.isNullOrBlank()) {
            throw IllegalArgumentException("El nombre de usuario no puede estar vacío")
        }

        if (userDto.email.isNullOrBlank()) {
            throw IllegalArgumentException("El email no puede estar vacío")
        }

        if (userRepository.existsByUsername(userDto.username!!)) {
            throw IllegalArgumentException("El nombre de usuario ya existe")
        }

        if (userRepository.existsByEmail(userDto.email!!)) {
            throw IllegalArgumentException("El email ya está registrado")
        }

        val user = UserMapper.toEntity(userDto)
        user.password = passwordEncoder.encode(userDto.password)

        return userRepository.save(user)
    }






    // -------------------------------
    // 🧩 Futuras funcionalidades:
    // -------------------------------

    /*
    fun create(userDto: UserDto): UserEntity {
        if (userDto.password.isNullOrBlank()) {
            throw Exception("La contraseña no puede estar vacía")
        }

        val user = UserMapper.toEntity(userDto)
        user.password = passwordEncoder.encode(userDto.password)
        return userRepository.save(user)
    }

    fun update(id: Long, userDto: UserDto): UserEntity {
        val existingUser = userRepository.findById(id).orElseThrow { Exception("Usuario no encontrado") }

        existingUser.username = userDto.username ?: existingUser.username
        existingUser.email = userDto.email ?: existingUser.email
        existingUser.locked = userDto.locked ?: existingUser.locked
        existingUser.disabled = userDto.disabled ?: existingUser.disabled

        // Si se quiere permitir actualizar contraseña:
        if (!userDto.password.isNullOrBlank()) {
            existingUser.password = passwordEncoder.encode(userDto.password)
        }

        return userRepository.save(existingUser)
    }
    */
}
