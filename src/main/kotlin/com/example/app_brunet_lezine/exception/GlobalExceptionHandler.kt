package com.example.app_brunet_lezine.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("message" to (ex.message ?: "Credenciales incorrectas")))
    }

    @ExceptionHandler(LockedException::class)
    fun handleLocked(ex: LockedException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.LOCKED).body(mapOf("message" to (ex.message ?: "Usuario bloqueado")))
    }

    @ExceptionHandler(DisabledException::class)
    fun handleDisabled(ex: DisabledException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(mapOf("message" to (ex.message ?: "Usuario deshabilitado")))
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUserNotFound(ex: UsernameNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("message" to (ex.message ?: "Usuario no encontrado")))
    }

    @ExceptionHandler(Exception::class)
    fun handleOther(ex: Exception): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("message" to (ex.message ?: "Error desconocido")))
    }
}
