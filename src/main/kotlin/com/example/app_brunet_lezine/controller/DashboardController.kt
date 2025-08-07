package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.config.JwtUtil
import com.example.app_brunet_lezine.service.EvaluationsService
import com.example.app_brunet_lezine.service.GlobalResultsService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    val evaluationsService: EvaluationsService,
    val globalResultsService: GlobalResultsService,
    val jwtUtil: JwtUtil
) {

    @GetMapping
    fun getDashboardInfo(request: HttpServletRequest): ResponseEntity<Any> {
        val token = request.getHeader("Authorization")?.removePrefix("Bearer ") ?: return ResponseEntity.status(401).build()
        val decoded = jwtUtil.validateToken(token)
        val role = jwtUtil.getSpecificClaim(decoded, "authorities").asString()
        val username = jwtUtil.extractUsername(decoded)

        return if (role.contains("ADMIN")) {
            ResponseEntity.ok(evaluationsService.getAdminDashboardData())
        } else {
            ResponseEntity.ok(evaluationsService.getEvaluatorDashboardDataByUsername(username))
        }
    }
}
