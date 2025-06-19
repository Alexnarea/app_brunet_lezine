package com.example.app_brunet_lezine.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

class AdministratorDto {
    var id: Long? = null

    @field:NotBlank(message = "fullName is required")
    @field:Size(max = 50)
    var fullName: String? = null

    @field:NotBlank(message = "nui is required")
    @field:Size(max = 15)
    var nui: String? = null

    var phone: String? = null

    @field:NotNull(message = "birthdate is required")
    var birthdate: LocalDate? = null

    var gender: String? = null
}
