package com.example.app_brunet_lezine.dto

import java.time.LocalDateTime

data class ReportDto(
    val id: Long? = null,
    val evaluationId: Long,
    val filePath: String? = null,
    val generatedAt: LocalDateTime? = null,

    // Datos del niño
    val childFullName: String,
    val childNui: String?,
    val childBirthdate: String?, // puedes usar LocalDate si quieres
    val childGender: String?,

    // Datos de la evaluación
    val applicationDate: LocalDateTime,
    val chronologicalAgeMonths: Int,
    val observaciones: String? = null,

    // Resultados globales
    val resultYears: String?,
    val resultDetail: String?,
    val coefficient: Double?,
    val classification: String?,
    val totalMonthsApproved: Double?,

    // Ítems evaluados
    val items: List<EvaluationItemDto>
)
