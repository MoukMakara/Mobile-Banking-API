package co.istad.mbanking.features.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank
        String actNoOfOwner,
        @NotBlank
        String actNoOfReceiver,
        @NotNull
        @Positive
        BigDecimal amount,
        String remark
) {
}
