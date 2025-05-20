package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.config.JwtUtil
import com.example.app_brunet_lezine.dto.LoginDto
import com.example.app_brunet_lezine.dto.TokenDto
import com.example.app_brunet_lezine.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
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

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Usuario no encontrado")

        // Sin roles, autoridades vacías
        val authorityList = emptyList<SimpleGrantedAuthority>()

        return org.springframework.security.core.userdetails.User.builder()
            .username(userEntity.username!!)
            .password(userEntity.password!!)
            .authorities(authorityList)
            .accountLocked(userEntity.locked ?: false)
            .disabled(userEntity.disabled ?: false)
            .build()
    }

    fun login(loginDto: LoginDto): TokenDto {
        val authentication = authenticate(loginDto.username!!, loginDto.password!!)
        SecurityContextHolder.getContext().authentication = authentication
        val accessToken = jwtUtil.create(authentication)
        return TokenDto().apply {
            jwt = accessToken
        }
    }

    fun authenticate(username: String, password: String): Authentication {
        val userDetails = loadUserByUsername(username)

        if (!passwordEncoder.matches(password, userDetails.password)) {
            throw UsernameNotFoundException("Credenciales incorrectas")
        }

        return UsernamePasswordAuthenticationToken(username, null, userDetails.authorities)
    }
}
