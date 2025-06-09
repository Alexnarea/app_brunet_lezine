package com.example.app_brunet_lezine.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class LoginDto {
    @NotNull(message = "Username is required")
    @NotBlank(message = "Username cannot be blank")
    var username: String? = null

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password cannot be blank")
    var password: String? = null
}
