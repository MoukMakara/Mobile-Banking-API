package co.istad.mbanking.features.account.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountTypeRequest(

        @NotBlank(message = "Alias is required")
        String alias,

        @NotBlank(message = "Name is required")
        String name,

        String description
) {
}