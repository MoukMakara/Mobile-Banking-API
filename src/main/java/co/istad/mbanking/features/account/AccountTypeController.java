package co.istad.mbanking.features.account;

import co.istad.mbanking.features.account.dto.AccountTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
