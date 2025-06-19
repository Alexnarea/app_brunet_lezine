package com.example.app_brunet_lezine.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    fun getAdminDashboard(): String {
        return "Vista del administrador"
    }
}