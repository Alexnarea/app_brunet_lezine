package com.example.app_brunet_lezine.config

import org.springframework.stereotype.Component
import java.util.*

@Component
class StartupChecker {

    init {
        println("🕒 Hora actual del servidor: ${Date()}")
    }
}