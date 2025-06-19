package com.example.app_brunet_lezine.dto

data class EvaluationResultDto(
    val evaluationId: Long,
    val totalMonthsApproved: Double,
    val coefficient: Double,
    val classification: String,
    val resultYears: String,
    val resultDetail: String
)
