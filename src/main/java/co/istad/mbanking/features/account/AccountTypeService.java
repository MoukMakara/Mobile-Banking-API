package co.istad.mbanking.features.account;

import co.istad.mbanking.features.account.dto.AccountTypeRequest;
import co.istad.mbanking.features.account.dto.AccountTypeResponse;
import co.istad.mbanking.features.account.dto.AccountTypeUpdateRequest;

import java.util.List;

public interface AccountTypeService {

    List<AccountTypeResponse> findAll();

    AccountTypeResponse findByAlias(String alias);

    AccountTypeResponse createNew(AccountTypeRequest accountTypeRequest);

    AccountTypeResponse updateByAlias(String alias, AccountTypeUpdateRequest accountTypeUpdateRequest);

    AccountTypeResponse deleteByAlias(String alias);


}
