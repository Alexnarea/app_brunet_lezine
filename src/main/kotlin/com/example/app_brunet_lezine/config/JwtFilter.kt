package com.example.app_brunet_lezine.config
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter


class JwtFilter(private val jwtUtil: JwtUtil) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        // Solo intenta validar si hay un token real
        if (!authHeader.isNullOrBlank() && authHeader.startsWith("Bearer ")) {
            val jwtToken = authHeader.removePrefix("Bearer ").trim()

            try {
                if (jwtToken.isNotBlank()) {
                    val decodedJWT: DecodedJWT = jwtUtil.validateToken(jwtToken)
                    val username: String = jwtUtil.extractUsername(decodedJWT)
                    val stringAuthorities: String = jwtUtil.getSpecificClaim(decodedJWT, "authorities").asString()

                    val authorities: Collection<GrantedAuthority> =
                        AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities)

                    val authenticationToken = UsernamePasswordAuthenticationToken(username, null, authorities)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                }
            } catch (ex: Exception) {
                logger.warn("Token inválido pero ignorado por configuración pública: ${ex.message}")
            }
        }

        filterChain.doFilter(request, response)
    }
}