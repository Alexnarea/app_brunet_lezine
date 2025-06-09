package com.example.app_brunet_lezine.dto

import java.time.LocalDateTime

class EvaluationsDto {
    var id: Long? = null
    var applicationDate: LocalDateTime? = null
    var chronologicalAgeMonths: Int? = null

    var childrenId: Long? = null
    var evaluatorId: Long? = null
}