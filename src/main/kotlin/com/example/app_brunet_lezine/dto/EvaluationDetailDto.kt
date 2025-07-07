package com.example.app_brunet_lezine.dto

import java.time.LocalDateTime

data class EvaluationDetailDto(
    val id: Long,
    val applicationDate: LocalDateTime,
    val chronologicalAgeMonths: Int,
    val childrenId: Long,
    val evaluatorId: Long,
    var resultYears: String,
    val coefficient: Double,
    val classification: String,
    val observaciones: String?,
    val items: List<EvaluationItemDto>
)

data class EvaluationItemDto(
    val id: Long,
    val task: String,
    val domain: String,
    val completed: Boolean,
    val referenceAgeMonths: Int // ✅ nuevo campo para agrupar ítems por edad

)
