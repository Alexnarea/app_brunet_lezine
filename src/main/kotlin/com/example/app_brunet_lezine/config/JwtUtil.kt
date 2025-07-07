package com.example.app_brunet_lezine.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.app_brunet_lezine.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class JwtUtil {

    private val SECRET_KEY = System.getenv("JWT_SECRET") ?: "s3cr3t"
    private val ALGORITHM: Algorithm = Algorithm.HMAC256(SECRET_KEY)

    @Autowired
    private lateinit var userRepository: UserRepository

    fun create(authentication: Authentication): String {
        val now = Date()
        val expiry = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15))

        // ⚠ Aquí accedemos correctamente al UserDetails
        val userDetails = authentication.principal as? UserDetails
            ?: throw IllegalStateException("El principal no es un UserDetails")

        val username = userDetails.username

        val userEntity = userRepository.findByUsername(username)
            ?: throw IllegalStateException("Usuario no encontrado al generar el token")

        val authorities = userDetails.authorities
            .joinToString(",") { it.authority }

        return JWT.create()
            .withSubject(username)
            .withIssuer("project-admin")
            .withIssuedAt(now)
            .withExpiresAt(expiry)
            .withClaim("authorities", authorities)
            .withClaim("userId", userEntity.id)
            .sign(ALGORITHM)
    }

    fun validateToken(token: String): DecodedJWT {
        return try {
            val verifier = JWT.require(ALGORITHM)
                .withIssuer("project-admin")
                .acceptLeeway(120) // 2 minutos de margen
                .build()

            verifier.verify(token)
        } catch (ex: JWTVerificationException) {
            throw JWTVerificationException("Token inválido: ${ex.message}")
        }
    }

    fun extractUsername(decodedJWT: DecodedJWT): String {
        return decodedJWT.subject
    }

    fun extractUserId(decodedJWT: DecodedJWT): Long {
        return decodedJWT.getClaim("userId").asLong()
            ?: throw IllegalStateException("El token no contiene el userId")
    }

    fun getSpecificClaim(decodedJWT: DecodedJWT, claimName: String): Claim {
        return decodedJWT.getClaim(claimName)
    }
}
