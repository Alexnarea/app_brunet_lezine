package com.example.app_brunet_lezine.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RootController {

    @GetMapping("/")
    fun home(): String {
        return "✅ API Brunet-Lézine funcionando correctamente"
    }
}
