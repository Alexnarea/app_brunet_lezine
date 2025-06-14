package com.example.app_brunet_lezine.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalDateTime

class ChildrenDto {
    var id: Long? = null

    @field:NotNull(message = "fullname is required")
    @field:NotBlank(message = "fullname cannot be blank")
    var fullName: String? = null

    var nui: String? = null
    var birthdate: LocalDate? = null
    var gender: String? = null
    var creationDate: LocalDateTime? = null
}
