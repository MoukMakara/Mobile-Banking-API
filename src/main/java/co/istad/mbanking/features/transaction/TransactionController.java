package co.istad.mbanking.features.transaction;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import co.istad.mbanking.features.transaction.dto.TransferRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "transaction-controller")
public class TransactionController {

    private final TransactionService transactionService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/transfers")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(@Valid @RequestBody TransferRequest transferRequest,
                                                                     Authentication auth) {
        TransactionResponse response = transactionService.transfer(transferRequest, auth);

        ApiResponse<TransactionResponse> apiResponse = ApiResponse.<TransactionResponse>builder()
                .success(true)
                .message("Transfer successful")
                .status(HttpStatus.CREATED)
                .payload(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }



}
