package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.dto.LoginDto
import com.example.app_brunet_lezine.service.UserSecurityService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @Autowired
    lateinit var userSecurityService: UserSecurityService

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginDto: LoginDto): ResponseEntity<*> {
        val response = userSecurityService.login(loginDto)
        return ResponseEntity.ok(response)
    }
}
