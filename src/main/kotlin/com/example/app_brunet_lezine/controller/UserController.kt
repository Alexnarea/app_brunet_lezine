package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.dto.LoginDto
import com.example.app_brunet_lezine.dto.UserDto
import com.example.app_brunet_lezine.mapper.UserMapper
import com.example.app_brunet_lezine.response.SuccessResponse
import com.example.app_brunet_lezine.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun listAll(): ResponseEntity<List<UserDto>> {
        val users = userService.list().map { UserMapper.toUserDto(it) }
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserDto> {
        val user = userService.listById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(UserMapper.toUserDto(user))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<SuccessResponse> {
        val result = userService.delete(id)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/update-password")
    fun updatePassword(@RequestBody loginDto: LoginDto): ResponseEntity<UserDto> {
        val updatedUser = userService.updatePassword(loginDto)
        return ResponseEntity.ok(UserMapper.toUserDto(updatedUser))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<UserDto> {
        val user = userService.login(loginDto)
        return ResponseEntity.ok(UserMapper.toUserDto(user))
    }

    // --------------------------------------------
    // Métodos para versiones futuras
    // --------------------------------------------


    @PostMapping
    fun create(@RequestBody userDto: UserDto): ResponseEntity<UserDto> {
        val createdUser = userService.create(userDto)
        return ResponseEntity.ok(UserMapper.toUserDto(createdUser))
    }
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody userDto: UserDto): ResponseEntity<UserDto> {
        val updatedUser = userService.update(id, userDto)
        return ResponseEntity.ok(UserMapper.toUserDto(updatedUser))
    }

}
