package com.example.app_brunet_lezine.config

import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.http.HttpHeaders
import org.springframework.util.AntPathMatcher

@Component
class JwtFilter(private val jwtUtil: JwtUtil) : OncePerRequestFilter() {

    private val pathMatcher = AntPathMatcher()

    // Ignora rutas públicas para evitar verificar tokens innecesariamente
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val publicPaths = listOf(
            "/auth/**",
            "/actuator/**",
            "/mqtt/**",
            "/client/**",
            "/children/**"
        )
        return publicPaths.any { pathMatcher.match(it, request.servletPath) }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (!authorizationHeader.isNullOrEmpty() && authorizationHeader.startsWith("Bearer ")) {
            val jwtToken = authorizationHeader.substring(7)

            try {
                val decodedJWT: DecodedJWT = jwtUtil.validateToken(jwtToken)
                val username: String = jwtUtil.extractUsername(decodedJWT)

                // Si en el futuro quieres roles, los extraes aquí desde el token
                val authorities: Collection<GrantedAuthority> = emptyList()

                val authenticationToken = UsernamePasswordAuthenticationToken(username, null, authorities)
                val context = SecurityContextHolder.createEmptyContext()
                context.authentication = authenticationToken
                SecurityContextHolder.setContext(context)

            } catch (ex: Exception) {
                logger.warn("Token inválido o expirado: ${ex.message}")
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }
}