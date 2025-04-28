package co.istad.mbanking.features.account;

import co.istad.mbanking.features.account.dto.*;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface AccountService {
    void updateTransferLimit(String actNo, AccountTransferLimitRequest accountTransferLimitRequest);

    void enableAccount(String actNo);

    void disableAccount(String actNo);

    void deleteByActNo(String actNo);

    Page<AccountDetailResponse> findAll(int pageNo, int pageSize);

    AccountDetailResponse updateByActNo(String actNo, UpdateAccountRequest updateAccountRequest);

    AccountDetailResponse findByActNo(String actNo);

    AccountDetailResponse deposit(String actNo, DepositRequest depositRequest);

    AccountDetailResponse withdraw(String actNo, WithdrawRequest withdrawRequest);

    void createNew(CreateAccountRequest createAccountRequest);

    AccountDetailResponse renameAccount(String actNo, AccountRenameRequest accountRenameRequest);

}
