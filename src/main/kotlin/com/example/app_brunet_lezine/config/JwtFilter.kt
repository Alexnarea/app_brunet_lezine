package com.example.app_brunet_lezine.config

import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(private val jwtUtil: JwtUtil) : OncePerRequestFilter() {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath

        // Ignora el filtro para rutas de autenticación
        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authHeader.isNullOrBlank() || !authHeader.startsWith(BEARER_PREFIX)) {
            // No token → pasar sin validar
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken = authHeader.removePrefix(BEARER_PREFIX).trim()

        try {
            val decodedJWT: DecodedJWT = jwtUtil.validateToken(jwtToken)
            val username: String = jwtUtil.extractUsername(decodedJWT)
            val stringAuthorities: String = jwtUtil.getSpecificClaim(decodedJWT, "authorities").asString()

            val authorities: Collection<GrantedAuthority> =
                AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities)

// 💡 Pasar un UserDetails al token
            val userDetails = org.springframework.security.core.userdetails.User(
                username,
                "",
                authorities
            )

            val authenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, authorities)
            SecurityContextHolder.getContext().authentication = authenticationToken

            logger.info("✅ Token válido para usuario $username con roles: $stringAuthorities")

        } catch (ex: Exception) {
            logger.warn("⚠ Token inválido: ${ex.message}")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido")
            return
        }

        filterChain.doFilter(request, response)
    }
}

