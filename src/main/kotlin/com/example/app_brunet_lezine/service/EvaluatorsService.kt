package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.EvaluatorsDto
import com.example.app_brunet_lezine.dto.SimpleUserDto
import com.example.app_brunet_lezine.dto.UserDto
import com.example.app_brunet_lezine.entity.Evaluators
import com.example.app_brunet_lezine.mapper.EvaluatorsMapper
import com.example.app_brunet_lezine.mapper.SimpleUserMapper
import com.example.app_brunet_lezine.mapper.UserMapper
import com.example.app_brunet_lezine.repository.EvaluatorsRepository
import com.example.app_brunet_lezine.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EvaluatorsService {

    @Autowired
    private lateinit var simpleUserMapper: SimpleUserMapper

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var evaluatorsMapper: EvaluatorsMapper

    @Autowired
    lateinit var evaluatorsRepository: EvaluatorsRepository

    @Autowired
    lateinit var userRepository: UserRepository

    fun findAll(): List<EvaluatorsDto> {
        val evaluators = evaluatorsRepository.findAll()
        return evaluators.map { EvaluatorsMapper.toEvaluatorsDto(it) }
    }

    fun findById(id: Long): EvaluatorsDto {
        val evaluators = evaluatorsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Evaluator with id $id not found") }
        return EvaluatorsMapper.toEvaluatorsDto(evaluators)
    }

    fun findByUsername(username: String): EvaluatorsDto {
        val entity = evaluatorsRepository.findByUserUsername(username)
            ?: throw EntityNotFoundException("Evaluador no encontrado para el usuario: $username")
        return evaluatorsMapper.toEvaluatorsDto(entity)
    }


    fun findByUserId(userId: Long): EvaluatorsDto {
        val evaluator = evaluatorsRepository.findByUserId(userId)
            ?: throw EntityNotFoundException("Evaluador no encontrado para el usuario $userId")

        return evaluatorsMapper.toEvaluatorsDto(evaluator)
    }

    fun findAvailableUsers(): List<SimpleUserDto> {
        return try {
            val allUsers = userRepository.findAll()
            val assignedUserIds = evaluatorsRepository.findAll().mapNotNull { it.user?.id }.toSet()

            allUsers
                .filter { user ->
                    val hasEvaluatorRole = user.roles?.any { it.role == "EVALUATOR" } == true
                    val notAssigned = user.id != null && user.id !in assignedUserIds

                    if (user.roles.isNullOrEmpty()) {
                        println("⚠ Usuario '${user.username}' no tiene roles asignados")
                    }

                    hasEvaluatorRole && notAssigned
                }
                .map { simpleUserMapper.toUserSimpleDto(it) }

        } catch (ex: Exception) {
            println("❌ Error en findAvailableUsers: ${ex.message}")
            throw IllegalStateException("No se pudo cargar la lista de usuarios disponibles: ${ex.message}")
        }
    }


    fun save(evaluatorDto: EvaluatorsDto): EvaluatorsDto {
        val userId = evaluatorDto.userId
            ?: throw IllegalArgumentException("userId is required")

        if (evaluatorsRepository.findByUserId(userId) != null) {
            throw IllegalArgumentException("Este usuario ya tiene un evaluador asignado")
        }

        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User with id $userId not found") }

        val evaluators = EvaluatorsMapper.toEntity(evaluatorDto, user)
        val saved = evaluatorsRepository.save(evaluators)
        return EvaluatorsMapper.toEvaluatorsDto(saved)
    }


    fun update(id: Long, evaluatorDto: EvaluatorsDto): EvaluatorsDto {
        val evaluator = evaluatorsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Evaluator with id $id not found") }

        evaluator.speciality = evaluatorDto.speciality
        evaluator.fullName = evaluatorDto.fullName
        evaluator.nui = evaluatorDto.nui
        evaluator.phone = evaluatorDto.phone
        evaluator.birthdate = evaluatorDto.birthdate
        evaluator.gender = evaluatorDto.gender

        if (evaluatorDto.userId != null && evaluator.user?.id != evaluatorDto.userId) {
            // Validar que el nuevo usuario no esté ya asignado a otro evaluador
            val existing = evaluatorsRepository.findByUserId(evaluatorDto.userId!!)
            if (existing != null) {
                throw IllegalArgumentException("Este usuario ya tiene un evaluador asignado")
            }

            val user = userRepository.findById(evaluatorDto.userId!!)
                .orElseThrow { EntityNotFoundException("User with id ${evaluatorDto.userId} not found") }

            evaluator.user = user
        }

        val updated = evaluatorsRepository.save(evaluator)
        return EvaluatorsMapper.toEvaluatorsDto(updated)
    }


    fun delete(id: Long) {
        val evaluator = evaluatorsRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Evaluator with id $id not found") }
        evaluatorsRepository.delete(evaluator)
    }
}
