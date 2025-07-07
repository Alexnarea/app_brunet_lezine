package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.dto.EvaluatorsDto
import com.example.app_brunet_lezine.response.SuccessResponse
import com.example.app_brunet_lezine.service.EvaluatorsService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/evaluators")
class EvaluatorsController {
    @Autowired
    lateinit var evaluatorsService: EvaluatorsService

    @GetMapping
    fun findAll(): ResponseEntity<*>{
        val response = evaluatorsService.findAll()
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<*>{
        val response = evaluatorsService.findById(id)
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.OK)
    }

    @GetMapping("/by-user-id/{userId}")
    fun findByUserId(@PathVariable userId: Long): ResponseEntity<EvaluatorsDto> {
        val evaluator = evaluatorsService.findByUserId(userId)
        return ResponseEntity.ok(evaluator)
    }

    @GetMapping("/by-username")
    fun findByUsername(@RequestParam username: String): ResponseEntity<EvaluatorsDto> {
        val evaluator = evaluatorsService.findByUsername(username)
        return ResponseEntity.ok(evaluator)
    }

    @GetMapping("/available-users")
    fun getAvailableUsers(): ResponseEntity<*> {
        val availableUsers = evaluatorsService.findAvailableUsers()
        return ResponseEntity(SuccessResponse(data = availableUsers), HttpStatus.OK)
    }

    @PostMapping
    fun save (@RequestBody @Valid evaluatorsDto: EvaluatorsDto): ResponseEntity<Any>{
        val response = evaluatorsService.save(evaluatorsDto)
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid evaluatorDto: EvaluatorsDto): ResponseEntity<Any>{
        val response = evaluatorsService.update(id, evaluatorDto)
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any>{
        val response = evaluatorsService.delete(id)
        return ResponseEntity(SuccessResponse(data = response), HttpStatus.OK)
    }


}