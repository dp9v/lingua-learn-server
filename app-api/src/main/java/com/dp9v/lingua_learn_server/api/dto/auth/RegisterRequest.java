package com.dp9v.lingua_learn_server.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email(message = "Email is not valid")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @Size(min = 2, message = "Name must be at least 2 characters long")
        String name,

        @Size(min = 2, message = "Name must be at least 2 characters long")
        String surname
) {
}
