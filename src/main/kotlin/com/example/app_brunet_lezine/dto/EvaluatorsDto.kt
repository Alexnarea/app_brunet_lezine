package com.example.app_brunet_lezine.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

class EvaluatorsDto {

    var id: Long? = null

    @field:NotBlank(message = "speciality cannot be blank")
    var speciality: String? = null

    @field:NotBlank(message = "fullName cannot be blank")
    var fullName: String? = null

    @field:NotBlank(message = "nui cannot be blank")
    var nui: String? = null

    var phone: String? = null

    @field:NotNull(message = "birthdate is required")
    var birthdate: LocalDate? = null

    var gender: String? = null

    @field:NotNull(message = "userId is required")
    var userId: Long? = null
}
