package co.istad.mbanking.features.account;

import co.istad.mbanking.features.account.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{actNo}/transfer-limit")
    void updateTransferLimit(@PathVariable("actNo") String actNo,
                             @Valid @RequestBody AccountTransferLimitRequest accountTransferLimitRequest) {
        accountService.updateTransferLimit(actNo, accountTransferLimitRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{actNo}/enable")
    void enableByActNo(@PathVariable String actNo) {
        accountService.enableAccount(actNo);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{actNo}/disable")
    void disableByActNo(@PathVariable String actNo) {
        accountService.disableAccount(actNo);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{actNo}")
    void deleteByActNo(@PathVariable String actNo) {
        accountService.deleteByActNo(actNo);
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping
    Page<AccountDetailResponse> findAll(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                        @RequestParam(required = false, defaultValue = "25") int pageSize) {
        return accountService.findAll(pageNo, pageSize);
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping("/{actNo}")
    AccountDetailResponse updateByActNo(@PathVariable String actNo,
                                        @RequestBody @Valid UpdateAccountRequest updateAccountRequest) {
        return accountService.updateByActNo(actNo, updateAccountRequest);
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{actNo}")
    AccountDetailResponse findByActNo(@PathVariable String actNo) {
        return accountService.findByActNo(actNo);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{actNo}/deposit")
    AccountDetailResponse deposit(@PathVariable String actNo,
                                        @RequestBody @Valid DepositRequest depositRequest) {
        return accountService.deposit(actNo, depositRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{actNo}/withdraw")
    AccountDetailResponse withdraw(@PathVariable String actNo,
                                        @RequestBody @Valid WithdrawRequest withdrawRequest) {
        return accountService.withdraw(actNo, withdrawRequest);
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createNew(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        accountService.createNew(createAccountRequest);
    }
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/{actNo}/rename")
    AccountDetailResponse renameAccount(@PathVariable("actNo") String actNo,
                                  @Valid @RequestBody AccountRenameRequest accountRenameRequest) {
        return accountService.renameAccount(actNo, accountRenameRequest);
    }

}
