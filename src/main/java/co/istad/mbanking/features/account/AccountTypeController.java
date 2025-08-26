package co.istad.mbanking.features.account;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.account.dto.AccountTypeRequest;
import co.istad.mbanking.features.account.dto.AccountTypeResponse;
import co.istad.mbanking.features.account.dto.AccountTypeUpdateRequest;
import co.istad.mbanking.security.CurrentUserUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account-types")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;
    private final CurrentUserUtil currentUserUtil;

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/{alias}")
    public ResponseEntity<ApiResponse<AccountTypeResponse>> findByAlias(@PathVariable String alias) {
        AccountTypeResponse response = accountTypeService.findByAlias(alias);

        ApiResponse<AccountTypeResponse> apiResponse = ApiResponse.<AccountTypeResponse>builder()
                .success(true)
                .message("Account type found")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER', 'ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountTypeResponse>>> findAll() {
        List<AccountTypeResponse> response = accountTypeService.findAll();

        ApiResponse<List<AccountTypeResponse>> apiResponse = ApiResponse.<List<AccountTypeResponse>>builder()
                .success(true)
                .message("All account types retrieved")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<AccountTypeResponse>> createNew(@Valid @RequestBody AccountTypeRequest accountTypeRequest) {
        AccountTypeResponse response = accountTypeService.createNew(accountTypeRequest);

        ApiResponse<AccountTypeResponse> apiResponse = ApiResponse.<AccountTypeResponse>builder()
                .success(true)
                .message("Account type created successfully")
                .status(HttpStatus.CREATED)
                .payload(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PatchMapping("/{alias}")
    public ResponseEntity<ApiResponse<AccountTypeResponse>> updateByAlias(@PathVariable String alias,
                                                                          @RequestBody AccountTypeUpdateRequest accountTypeUpdateRequest) {
        AccountTypeResponse response = accountTypeService.updateByAlias(alias, accountTypeUpdateRequest);

        ApiResponse<AccountTypeResponse> apiResponse = ApiResponse.<AccountTypeResponse>builder()
                .success(true)
                .message("Account type updated successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @DeleteMapping("/{alias}")
    public ResponseEntity<ApiResponse<AccountTypeResponse>> deleteByAlias(@PathVariable("alias") String alias) {
        AccountTypeResponse response = accountTypeService.deleteByAlias(alias);

        ApiResponse<AccountTypeResponse> apiResponse = ApiResponse.<AccountTypeResponse>builder()
                .success(true)
                .message("Account type deleted successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    // New endpoint for getting account types for current user
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<AccountTypeResponse>>> getAccountTypesForCurrentUser() {
        // Get the current authenticated user's UUID
        String currentUserUuid = currentUserUtil.getCurrentUserUuid();

        // Get account types associated with the current user
        List<AccountTypeResponse> response = accountTypeService.findAccountTypesByUserUuid(currentUserUuid);

        ApiResponse<List<AccountTypeResponse>> apiResponse = ApiResponse.<List<AccountTypeResponse>>builder()
                .success(true)
                .message("Current user account types retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
