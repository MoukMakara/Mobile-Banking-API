package co.istad.mbanking.features.account.dto;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record DepositRequest(
        @Min(value = 1, message = "Amount must be greater than 0")
        BigDecimal amount
) {
}
