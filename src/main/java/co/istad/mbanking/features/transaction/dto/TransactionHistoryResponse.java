package co.istad.mbanking.features.transaction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record TransactionHistoryResponse(
    String accountNo,
    List<TransactionResponse> transactions,
    int page,
    int size,
    int totalPages
) {
}
