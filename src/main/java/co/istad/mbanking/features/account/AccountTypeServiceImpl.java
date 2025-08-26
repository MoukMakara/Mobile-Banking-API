package co.istad.mbanking.features.account;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.account.dto.AccountTypeRequest;
import co.istad.mbanking.features.account.dto.AccountTypeResponse;
import co.istad.mbanking.features.account.dto.AccountTypeUpdateRequest;
import co.istad.mbanking.mapper.AccountTypeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;
    private final AccountTypeMapper accountTypeMapper;
    private final UserAccountRepository userAccountRepository;

    @Override
    public List<AccountTypeResponse> findAll() {

        List<AccountType> accountTypes = accountTypeRepository.findAll();

        return accountTypeMapper.toAccountTypeResponseList(accountTypes);
    }

    @Override
    public AccountTypeResponse findByAlias(String alias) {
        AccountType accountType = accountTypeRepository.findByAlias(alias)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Account type has not been found!"));

        return accountTypeMapper.toAccountTypeResponse(accountType);

    }
    @Override
    public AccountTypeResponse createNew(AccountTypeRequest accountTypeRequest) {

        // Validate alias
        if (accountTypeRepository.existsByAlias(accountTypeRequest.alias())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account type alias has already existed");
        }

        AccountType accountType = accountTypeMapper.fromAccountTypeRequest(accountTypeRequest);
        accountType.setIsDeleted(false);

        accountTypeRepository.save(accountType);

        return accountTypeMapper.toAccountTypeResponse(accountType);
    }

    @Override
    public AccountTypeResponse updateByAlias(String alias, AccountTypeUpdateRequest accountTypeUpdateRequest) {

        // Validate alias
        AccountType accountType = accountTypeRepository
                .findByAlias(alias)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account type alias has not been found"));

        log.info("Before map: {}, {}, {}", accountType.getId(), accountType.getDescription(), accountType.getIsDeleted());
        accountTypeMapper.fromAccountTypeUpdateRequest(accountTypeUpdateRequest, accountType);
        log.info("After map: {}, {}, {}", accountType.getId(), accountType.getDescription(), accountType.getIsDeleted());

        accountType = accountTypeRepository.save(accountType);

        return accountTypeMapper.toAccountTypeResponse(accountType);
    }

    @Override
    public AccountTypeResponse deleteByAlias(String alias) {

        // Validate alias
        AccountType accountType = accountTypeRepository
                .findByAlias(alias)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account type alias has not been found"));

        accountTypeRepository.delete(accountType);

        return accountTypeMapper.toAccountTypeResponse(accountType);
    }

    @Override
    public List<AccountTypeResponse> findAccountTypesByUserId(Integer userId) {
        // We should find the user's UUID from their ID
        // For now, let's implement using UUID directly
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                "Method not implemented. Use findAccountTypesByUserUuid instead.");
    }

    public List<AccountTypeResponse> findAccountTypesByUserUuid(String userUuid) {
        // Find accounts associated with the user UUID
        List<Account> userAccounts = userAccountRepository.findActiveAccountsByUserUuid(userUuid);

        // Extract unique account types from the accounts
        List<AccountType> accountTypes = userAccounts.stream()
                .map(Account::getAccountType)
                .filter(accountType -> accountType != null && !accountType.getIsDeleted())
                .distinct()
                .collect(Collectors.toList());

        // Map account types to response DTOs
        return accountTypeMapper.toAccountTypeResponseList(accountTypes);
    }

}
