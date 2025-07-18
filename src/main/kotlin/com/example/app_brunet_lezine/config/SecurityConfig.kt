package com.example.app_brunet_lezine.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtUtil: JwtUtil
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    // Rutas públicas
                    .requestMatchers("/", "/api/auth/**").permitAll()

                    // Accesibles por ADMIN y EVALUATOR (todos los métodos)
                    .requestMatchers(
                        "/api/children/**",
                        "/api/evaluations/**",
                        "/api/test-items/**",
                        "/api/responses/**",
                        "/api/global-results/**"
                    ).hasAnyRole("ADMIN", "EVALUATOR")

                    // Solo ADMIN puede acceder a gestión de usuarios y evaluadores
                    .requestMatchers("/api/users/**").hasRole("ADMIN")
                    .requestMatchers("/api/evaluators/**").hasRole("ADMIN")

                    // Cualquier otra ruta requiere estar autenticado (no se limita solo a ADMIN)
                    .anyRequest().authenticated()
            }
            .addFilterBefore(JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
