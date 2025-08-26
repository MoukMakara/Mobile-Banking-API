package co.istad.mbanking.features.transaction.dto;

import co.istad.mbanking.features.account.dto.AccountDetailResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionResponse(
        String actNoOfOwner,
        String actNoOfReceiver,
        String paymentReceiver,
        BigDecimal amount,
        String remark,
        String transactionType,
        LocalDateTime transactionAt,
        Boolean status
) {
    // Custom constructor to avoid duplication of receiver information
    public TransactionResponse {
        // If both fields contain the same value, prioritize actNoOfReceiver for internal accounts
        if (actNoOfReceiver != null && paymentReceiver != null && actNoOfReceiver.equals(paymentReceiver)) {
            paymentReceiver = null;
        }
    }
}
