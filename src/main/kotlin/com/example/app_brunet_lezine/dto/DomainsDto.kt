package com.example.app_brunet_lezine.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class DomainsDto {
    var id: Long? = null

    @NotNull(message = "descriptionDomain is required")
    @NotBlank(message = "descriptionDomain cannot be blank")
    var descriptionDomain: String? = null
}