package co.istad.mbanking.features.account;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.domain.UserAccount;
import co.istad.mbanking.features.account.dto.*;
import co.istad.mbanking.features.user.UserRepository;
import co.istad.mbanking.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;
    private final AccountRepository accountRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final AccountMapper accountMapper;


    @Override
    public AccountDetailResponse updateTransferLimit(String actNo, AccountTransferLimitRequest accountTransferLimitRequest) {

        // Validate account
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account has not been found"));

        account.setTransferLimit(accountTransferLimitRequest.amount());
        accountRepository.save(account);

        return accountMapper.toAccountDetailResponse(account);
//        return accountMapper.toAccountDetailResponse(accountRepository.save(account));

    }

    @Override
    public AccountDetailResponse enableAccount(String actNo) {

        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account doesn't exist"
                ));

        account.setIsDeleted(false);
        accountRepository.save(account);
        return accountMapper.toAccountDetailResponse(account);
    }


    @Override
    public AccountDetailResponse disableAccount(String actNo) {

        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account doesn't exist"
                ));

        account.setIsDeleted(true);
        accountRepository.save(account);

        return accountMapper.toAccountDetailResponse(account);
    }

    @Override
    public AccountDetailResponse deleteByActNo(String actNo) {

        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account doesn't exist"
                ));

        accountRepository.delete(account);

        return accountMapper.toAccountDetailResponse(account);
    }


    @Override
    public Page<AccountDetailResponse> findAll(int pageNo, int pageSize) {

        if (pageNo < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid page number");
        }

        if (pageSize < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid page size");
        }

        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sortById);

        Page<Account> accountsByPage = accountRepository.findAll(pageRequest);

        return accountsByPage.map(accountMapper::toAccountDetailResponse);
    }


    @Override
    public AccountDetailResponse updateByActNo(String actNo, UpdateAccountRequest updateAccountRequest) {

        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account doesn't exist"
                ));

        accountMapper.fromUpdateAccountRequestPartially(updateAccountRequest, account);

        account = accountRepository.save(account);

        return accountMapper.toAccountDetailResponse(account);
    }


    @Override
    public AccountDetailResponse findByActNo(String actNo) {

        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account doesn't exist"
                ));

        return accountMapper.toAccountDetailResponse(account);
    }

    @Override
    public AccountDetailResponse deposit(String actNo, DepositRequest depositRequest) {
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account doesn't exist"
                ));

        BigDecimal total = account.getBalance().add(depositRequest.amount());

        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        return accountMapper.toAccountDetailResponse(savedAccount);
    }

    @Override
    public AccountDetailResponse withdraw(String actNo, WithdrawRequest withdrawRequest) {
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account doesn't exist"
                ));
        if (account.getBalance().compareTo(withdrawRequest.amount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Insufficient balance");
        }else {
            BigDecimal total = account.getBalance().subtract(withdrawRequest.amount());
            account.setBalance(total);
            Account savedAccount = accountRepository.save(account);

            return accountMapper.toAccountDetailResponse(savedAccount);
        }
    }

    @Override
    public AccountDetailResponse createNew(CreateAccountRequest createAccountRequest) {

        // Validate account type
        AccountType accountType = accountTypeRepository
                .findByAlias(createAccountRequest.accountTypeAlias())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account type not found"
                ));

        // Validate account NO
        if (accountRepository.existsByActNo(createAccountRequest.actNo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account NO already exists");
        }

        // Validate user phone number
        User user = userRepository
                .findByPhoneNumber(createAccountRequest.phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Phone number doesn't exist"
                ));

        Account account = accountMapper.fromCreateAccountRequest(createAccountRequest);
        account.setTransferLimit(BigDecimal.valueOf(5000));
        account.setAccountType(accountType);
        account.setIsHidden(false);
        account.setIsDeleted(false);

        UserAccount userAccount = new UserAccount();
        userAccount.setUser(user);
        userAccount.setAccount(account);
        userAccount.setCreatedAt(LocalDateTime.now());
        userAccount.setIsDeleted(false);
        userAccount.setIsBlocked(false);

        userAccountRepository.save(userAccount);

        return accountMapper.toAccountDetailResponse(accountRepository.save(account));
    }

    @Override
    public AccountDetailResponse renameAccount(String actNo, AccountRenameRequest accountRenameRequest) {

        // Validate account
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account has not been found"));

        account.setAliasName(accountRenameRequest.aliasName());
        account = accountRepository.save(account);

        return accountMapper.toAccountDetailResponse(account);
    }

}
