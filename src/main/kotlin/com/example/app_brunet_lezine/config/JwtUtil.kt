package com.example.app_brunet_lezine.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

@Component
class JwtUtil {

    private val SECRET_KEY = "s3cr3t"
    private val ALGORITHM: Algorithm = Algorithm.HMAC256(SECRET_KEY)

    fun create(authentication: Authentication): String? {

        val authorities = authentication.authorities
            .stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))
        return JWT.create()
            .withClaim("authorities", authorities)
            .withSubject(authentication.principal.toString() )
            .withIssuer("project-admin")
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15)))
            .sign(ALGORITHM)
    }

    fun validateToken(token: String?): DecodedJWT {
        val algorithm: Algorithm = Algorithm.HMAC256(SECRET_KEY)
        val verifier = JWT.require(algorithm)
            .withIssuer("project-admin")
            .build()
        val decodedJWT: DecodedJWT = verifier.verify(token)
            ?: throw JWTVerificationException("Token invalid, not Authorized j")
        return decodedJWT
    }

    fun extractUsername(decodedJWT: DecodedJWT): String {
        return decodedJWT.subject.toString()
    }

    fun getSpecificClaim(decodedJWT: DecodedJWT, claimName: String?): Claim {
        return decodedJWT.getClaim(claimName)
    }
}