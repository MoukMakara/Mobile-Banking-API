package co.istad.mbanking.features.account;

import co.istad.mbanking.features.account.dto.AccountTypeResponse;

import java.util.List;

public interface AccountTypeService {

    List<AccountTypeResponse> findAll();

    AccountTypeResponse findByAlias(String alias);
}
