package co.istad.mbanking.features.account.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountTransferLimitRequest(
        @NotNull(message = "Amount is required")
        @Min(1000)
        BigDecimal amount
) {
}