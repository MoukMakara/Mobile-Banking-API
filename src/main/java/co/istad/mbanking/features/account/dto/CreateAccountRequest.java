package co.istad.mbanking.features.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank(message = "Account NO is required")
        @Size(message = "Account NO must be 9 digits", max = 9, min = 9)
        String actNo,
        @Positive(message = "Balance must be greater than 0")
        @NotNull(message = "First balance must be 5 dollars up")
        BigDecimal balance,
        @NotBlank(message = "Account type is required")
        String accountTypeAlias,
        @NotBlank(message = "Phone number is required")
        String phoneNumber
) {
}
