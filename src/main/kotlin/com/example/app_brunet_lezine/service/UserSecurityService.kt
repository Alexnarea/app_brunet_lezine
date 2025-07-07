package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.config.JwtUtil
import com.example.app_brunet_lezine.dto.LoginDto
import com.example.app_brunet_lezine.dto.TokenDto
import com.example.app_brunet_lezine.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserSecurityService : UserDetailsService {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    private val logger = LoggerFactory.getLogger(UserSecurityService::class.java)

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Usuario no encontrado")

        val authorityList = userEntity.roles?.map {
            SimpleGrantedAuthority("ROLE_${it.role}")
        } ?: emptyList()

        return org.springframework.security.core.userdetails.User.builder()
            .username(userEntity.username!!)
            .password(userEntity.password!!)
            .authorities(authorityList)
            .accountLocked(userEntity.locked ?: false)
            .disabled(userEntity.disabled ?: false)
            .build()
    }

    fun login(loginDto: LoginDto): TokenDto {
        val username = loginDto.username ?: throw BadCredentialsException("El nombre de usuario es requerido")
        val password = loginDto.password ?: throw BadCredentialsException("La contraseña es requerida")

        logger.info("Intentando login para usuario: $username")

        val authentication = authenticate(username, password)
        SecurityContextHolder.getContext().authentication = authentication
        val accessToken = jwtUtil.create(authentication)

        logger.info("Login exitoso, token generado")

        return TokenDto().apply {
            jwt = accessToken
        }
    }


    fun authenticate(username: String, password: String): Authentication {
        val userDetails = loadUserByUsername(username)

        if (userDetails is org.springframework.security.core.userdetails.User) {
            if (!userDetails.isAccountNonLocked) {
                throw LockedException("Usuario bloqueado")
            }
            if (!userDetails.isEnabled) {
                throw DisabledException("Usuario deshabilitado")
            }
        }

        if (!passwordEncoder.matches(password, userDetails.password)) {
            throw BadCredentialsException("Credenciales incorrectas")
        }

        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }
}

