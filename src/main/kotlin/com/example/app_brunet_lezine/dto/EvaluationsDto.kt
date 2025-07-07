package com.example.app_brunet_lezine.dto

import java.time.LocalDateTime

class EvaluationsDto {
    var id: Long? = null
    var applicationDate: LocalDateTime? = null
    var chronologicalAgeMonths: Int? = null

    var childrenId: Long? = null
    var evaluatorId: Long? = null

    var resultYears: String? = null
    var resultDetail: String? = null
    var coefficient: Double? = null
    var classification: String? = null

}