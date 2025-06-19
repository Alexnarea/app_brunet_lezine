package com.example.app_brunet_lezine.repository

import com.example.app_brunet_lezine.entity.Administrator
import org.springframework.data.jpa.repository.JpaRepository

interface AdministratorRepository: JpaRepository<Administrator, Long> {
}