package co.istad.mbanking.features.transaction.dto;

import co.istad.mbanking.features.account.dto.AccountDetailResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String actNoOfOwner,
        String actNoOfReceiver,
        BigDecimal amount,
        String remark,
        String transactionType,
        LocalDateTime transactionAt,
        Boolean status
) {
}
