package com.example.app_brunet_lezine.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class EvaluatorsDto {
    var id: Long? = null

    @NotNull(message = "speciality is required")
    @NotBlank(message = "speciality cannot be blank")
    var speciality: String? = null
}