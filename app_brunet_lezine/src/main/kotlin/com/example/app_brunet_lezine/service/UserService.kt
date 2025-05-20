package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.LoginDto
import com.example.app_brunet_lezine.entity.UserEntity
import com.example.app_brunet_lezine.repository.UserRepository
import com.example.app_brunet_lezine.response.SuccessResponse
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.SQLException

@Service
class UserService(
    private val userRepository: UserRepository,
    private var passwordEncoder: PasswordEncoder
) {

    fun list(): List<UserEntity> = userRepository.findAll()

    fun listById(id: Long?): UserEntity? = id?.let { userRepository.findById(it).orElse(null) }

    fun updatePassword(loginDto: LoginDto): UserEntity {
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
            ?: throw Exception("Usuario no encontrado")

        if (user.locked == true) throw Exception("Usuario bloqueado")
        if (user.disabled == true) throw Exception("Usuario deshabilitado")

        if (!passwordEncoder.matches(loginDto.password, user.password)) {
            throw Exception("Contraseña incorrecta")
        }
        return user
    }
}

