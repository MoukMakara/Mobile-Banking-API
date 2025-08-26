package co.istad.mbanking.features.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotBlank(message = "Account number of owner is required")
        String actNoOfOwner,

        @NotBlank(message = "Payment receiver ID is required")
        String paymentReceiver,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount,

        String remark
) {}
