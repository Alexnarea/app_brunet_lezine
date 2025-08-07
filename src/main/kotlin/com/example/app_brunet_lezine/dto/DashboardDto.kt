package com.example.app_brunet_lezine.dto

import java.math.BigDecimal
import java.time.LocalDate

// Resumen por evaluador (para admins)
data class EvaluatorSummaryDto(
    val evaluatorName: String,
    val childrenCount: Int
)

// Información de niños con retraso
data class ChildWithDelayDto(
    val childName: String,
    val age: Int,  // Edad cronológica en años
    val lastEvaluationDate: LocalDate,
    val coefficient: BigDecimal,
    val evaluatorName: String
)

// Dashboard para rol ADMIN
data class AdminDashboardDto(
    val evaluators: List<EvaluatorSummaryDto>,
    val childrenWithDelay: List<ChildWithDelayDto>,
    val childrenWithSevereDelay: List<ChildWithDelayDto>,
    val totalUsers: Int,
    val totalEvaluators: Int,
    val totalChildren: Int
)

// Dashboard para rol EVALUATOR
data class EvaluatorDashboardDto(
    val totalChildren: Int,
    val childrenWithDelay: List<ChildWithDelayDto>,
    val childrenWithSevereDelay: List<ChildWithDelayDto> // ✅ NUEVO
)
