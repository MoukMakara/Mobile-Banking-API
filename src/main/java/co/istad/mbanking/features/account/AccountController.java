package co.istad.mbanking.features.account;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.account.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PutMapping("/{actNo}/transfer-limit")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> updateTransferLimit(
            @PathVariable("actNo") String actNo,
            @Valid @RequestBody AccountTransferLimitRequest accountTransferLimitRequest) {

        AccountDetailResponse response = accountService.updateTransferLimit(actNo, accountTransferLimitRequest);

        ApiResponse<AccountDetailResponse> apiResponse = ApiResponse.<AccountDetailResponse>builder()
                .success(true)
                .message("Transfer limit updated successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PutMapping("/{actNo}/enable")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> enableByActNo(@PathVariable String actNo) {
        AccountDetailResponse response = accountService.enableAccount(actNo);

        ApiResponse<AccountDetailResponse> apiResponse = ApiResponse.<AccountDetailResponse>builder()
                .success(true)
                .message("Account enabled successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @DeleteMapping("/{actNo}/disable")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> disableByActNo(@PathVariable String actNo) {
        AccountDetailResponse response = accountService.disableAccount(actNo);

        ApiResponse<AccountDetailResponse> apiResponse = ApiResponse.<AccountDetailResponse>builder()
                .success(true)
                .message("Account disabled successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @DeleteMapping("/{actNo}")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> deleteByActNo(@PathVariable String actNo) {
        AccountDetailResponse response = accountService.deleteByActNo(actNo);

        ApiResponse<AccountDetailResponse> apiResponse = ApiResponse.<AccountDetailResponse>builder()
                .success(true)
                .message("Account deleted successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AccountDetailResponse>>> findAll(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                                                            @RequestParam(required = false, defaultValue = "25") int pageSize) {
        Page<AccountDetailResponse> response = accountService.findAll(pageNo, pageSize);

        ApiResponse<Page<AccountDetailResponse>> apiResponse = ApiResponse.<Page<AccountDetailResponse>>builder()
                .success(true)
                .message("Accounts retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @PatchMapping("/{actNo}")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> updateByActNo(@PathVariable String actNo,
                                                                            @RequestBody @Valid UpdateAccountRequest updateAccountRequest) {
        AccountDetailResponse response = accountService.updateByActNo(actNo, updateAccountRequest);

        ApiResponse<AccountDetailResponse> apiResponse = ApiResponse.<AccountDetailResponse>builder()
                .success(true)
                .message("Account updated successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/{actNo}")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> findByActNo(@PathVariable String actNo) {
        AccountDetailResponse response = accountService.findByActNo(actNo);

        ApiResponse<AccountDetailResponse> apiResponse = ApiResponse.<AccountDetailResponse>builder()
                .success(true)
                .message("Account retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{actNo}/deposit")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> deposit(@PathVariable String actNo,
                                                                      @RequestBody @Valid DepositRequest depositRequest) {
        AccountDetailResponse response = accountService.deposit(actNo, depositRequest);

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
    @PostMapping("/{actNo}/withdraw")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> withdraw(@PathVariable String actNo,
                                                                       @RequestBody @Valid WithdrawRequest withdrawRequest) {
        AccountDetailResponse response = accountService.withdraw(actNo, withdrawRequest);

        ApiResponse<AccountDetailResponse> apiResponse = ApiResponse.<AccountDetailResponse>builder()
                .success(true)
                .message("Withdrawal successful")
                .status(HttpStatus.CREATED)
                .payload(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<ApiResponse<AccountDetailResponse>> createNew(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        AccountDetailResponse response = accountService.createNew(createAccountRequest);

        ApiResponse<AccountDetailResponse> apiResponse = ApiResponse.<AccountDetailResponse>builder()
                .success(true)
                .message("Account created successfully")
                .status(HttpStatus.CREATED)
                .payload(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @PutMapping("/{actNo}/rename")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> renameAccount(@PathVariable("actNo") String actNo,
                                                                            @Valid @RequestBody AccountRenameRequest accountRenameRequest) {
        AccountDetailResponse response = accountService.renameAccount(actNo, accountRenameRequest);

        ApiResponse<AccountDetailResponse> apiResponse = ApiResponse.<AccountDetailResponse>builder()
                .success(true)
                .message("Account renamed successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // New endpoint for getting current user's accounts
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current-accounts")
    public ResponseEntity<ApiResponse<List<AccountDetailResponse>>> getCurrentUserAccounts() {
        List<AccountDetailResponse> response = accountService.findCurrentUserAccounts();

        ApiResponse<List<AccountDetailResponse>> apiResponse = ApiResponse.<List<AccountDetailResponse>>builder()
                .success(true)
                .message("Current user accounts retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
