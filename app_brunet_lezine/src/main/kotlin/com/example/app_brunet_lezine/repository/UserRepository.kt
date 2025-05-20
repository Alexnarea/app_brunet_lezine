package com.example.app_brunet_lezine.repository

import com.example.app_brunet_lezine.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
    fun findById (id: Long?): UserEntity?
}