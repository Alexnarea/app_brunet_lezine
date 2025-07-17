package com.example.app_brunet_lezine.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:5173","https://app.brunetlezinetest.com")  // Sin la barra al final
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH")
            allowedHeaders = listOf("*")
            allowCredentials = true
            maxAge = 3600L  // Opcional, cachea la pre-solicitud una hora
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }
}
