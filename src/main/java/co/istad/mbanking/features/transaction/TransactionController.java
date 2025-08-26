package co.istad.mbanking.features.transaction;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.account.dto.AccountDetailResponse;
import co.istad.mbanking.features.account.dto.DepositRequest;
import co.istad.mbanking.features.account.dto.WithdrawRequest;
import co.istad.mbanking.features.transaction.dto.PaymentRequest;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import co.istad.mbanking.features.transaction.dto.TransferRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/payments")
    public ResponseEntity<ApiResponse<TransactionResponse>> payment(@Valid @RequestBody PaymentRequest paymentRequest,
                                                                    Authentication auth) {
        TransactionResponse response = transactionService.payment(paymentRequest, auth);

        ApiResponse<TransactionResponse> apiResponse = ApiResponse.<TransactionResponse>builder()
                .success(true)
                .message("Payment successful")
                .status(HttpStatus.CREATED)
                .payload(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/accounts/{actNo}/deposit")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> deposit(@PathVariable String actNo,
                                                                      @RequestBody @Valid DepositRequest depositRequest) {
        AccountDetailResponse response = transactionService.deposit(actNo, depositRequest);

        ApiResponse<AccountDetailResponse> apiResponse = ApiResponse.<AccountDetailResponse>builder()
                .success(true)
                .message("Deposit successful")
                .status(HttpStatus.CREATED)
                .payload(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/accounts/{actNo}/withdraw")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> withdraw(@PathVariable String actNo,
                                                                       @RequestBody @Valid WithdrawRequest withdrawRequest) {
        AccountDetailResponse response = transactionService.withdraw(actNo, withdrawRequest);

        ApiResponse<AccountDetailResponse> apiResponse = ApiResponse.<AccountDetailResponse>builder()
                .success(true)
                .message("Withdrawal successful")
                .status(HttpStatus.CREATED)
                .payload(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
