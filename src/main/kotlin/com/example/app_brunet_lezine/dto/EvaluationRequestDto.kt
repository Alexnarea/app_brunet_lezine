package com.example.app_brunet_lezine.dto

data class EvaluationRequestDto(
    val childrenId: Long,
    val evaluatorId: Long,
    val chronologicalAgeMonths: Int,
    val responses: List<ResponseDto>
)

data class ResponseDto(
    val itemId: Long,
    val passed: Boolean
)
