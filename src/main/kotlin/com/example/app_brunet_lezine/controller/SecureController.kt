package com.example.app_brunet_lezine.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/secure")
class SecureController {

    @GetMapping("/hello")
    fun hello(): String {
        return "✅ ¡Accediste al endpoint seguro con un token válido!"
    }

    @GetMapping("/admin")
    fun adminOnly(): String {
        return "✅ ¡Accediste a un recurso reservado para admin o roles especiales!"
    }
}
