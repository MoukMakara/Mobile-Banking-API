package co.istad.mbanking.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record ReVerifyRequest(
        @NotBlank(message = "Email is required")
        String email
) {
}
