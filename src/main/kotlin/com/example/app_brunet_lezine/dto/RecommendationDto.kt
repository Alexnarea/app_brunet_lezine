package com.example.app_brunet_lezine.dto

data class RecommendationDto(
    var id: Long? = null,
    var resultId: Long,
    var activity: String,
    var frequency: String?
)
