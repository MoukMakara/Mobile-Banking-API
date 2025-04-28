package co.istad.mbanking.features.account;

import co.istad.mbanking.features.account.dto.AccountTypeRequest;
import co.istad.mbanking.features.account.dto.AccountTypeResponse;
import co.istad.mbanking.features.account.dto.AccountTypeUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account-types")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @GetMapping("/{alias}")
    AccountTypeResponse findByAlias(@PathVariable String alias) {
        return accountTypeService.findByAlias(alias);
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping
    List<AccountTypeResponse> findAll() {
        return accountTypeService.findAll();
    }
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createNew(@Valid @RequestBody AccountTypeRequest accountTypeRequest) {
        accountTypeService.createNew(accountTypeRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{alias}")
    void updateByAlias(@PathVariable String alias,
                                      @RequestBody AccountTypeUpdateRequest accountTypeUpdateRequest) {
        accountTypeService.updateByAlias(alias, accountTypeUpdateRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{alias}")
    void deleteByAlias(@PathVariable("alias") String alias) {
        accountTypeService.deleteByAlias(alias);
    }

}
