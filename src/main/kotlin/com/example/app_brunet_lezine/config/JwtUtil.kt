package com.example.app_brunet_lezine.config
import org.springframework.stereotype.Component
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.core.Authentication
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class JwtUtil {

    private val SECRET_KEY = "s3cr3t"
    private val ALGORITHM: Algorithm = Algorithm.HMAC256(SECRET_KEY)

    fun create(authentication: Authentication): String {
        // Si no usas roles, no es necesario incluir authorities en el token
        return JWT.create()
            .withSubject(authentication.name) // username directo
            .withIssuer("project-admin")
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15)))
            .sign(ALGORITHM)
    }

    fun validateToken(token: String?): DecodedJWT {
        if (token.isNullOrEmpty()) {
            throw JWTVerificationException("Token is null or empty")
        }

        val verifier = JWT.require(ALGORITHM)
            .withIssuer("project-admin")
            .build()

        return verifier.verify(token) ?: throw JWTVerificationException("Token invalid, not Authorized")
    }

    fun extractUsername(decodedJWT: DecodedJWT): String = decodedJWT.subject

    fun getSpecificClaim(decodedJWT: DecodedJWT, claimName: String): Claim = decodedJWT.getClaim(claimName)
}
