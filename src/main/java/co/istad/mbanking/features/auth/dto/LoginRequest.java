package co.istad.mbanking.features.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email
        @NotBlank(message = "Email is required")
        String email,
        @NotBlank(message = "Password is required")
        String password
) {
}
