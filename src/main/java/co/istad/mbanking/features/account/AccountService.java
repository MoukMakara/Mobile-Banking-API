package co.istad.mbanking.features.account;

import co.istad.mbanking.features.account.dto.*;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    AccountDetailResponse updateTransferLimit(String actNo, AccountTransferLimitRequest accountTransferLimitRequest);

    AccountDetailResponse enableAccount(String actNo);

    AccountDetailResponse disableAccount(String actNo);

    AccountDetailResponse deleteByActNo(String actNo);

    Page<AccountDetailResponse> findAll(int pageNo, int pageSize);

    List<AccountDetailResponse> findCurrentUserAccounts();

    AccountDetailResponse updateByActNo(String actNo, UpdateAccountRequest updateAccountRequest);

    AccountDetailResponse findByActNo(String actNo);

//    AccountDetailResponse deposit(String actNo, DepositRequest depositRequest);
//
//    AccountDetailResponse withdraw(String actNo, WithdrawRequest withdrawRequest);

    AccountDetailResponse createNew(CreateAccountRequest createAccountRequest);

    AccountDetailResponse renameAccount(String actNo, AccountRenameRequest accountRenameRequest);

}
