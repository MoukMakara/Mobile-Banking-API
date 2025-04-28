package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.features.account.dto.AccountDetailResponse;
import co.istad.mbanking.features.account.dto.CreateAccountRequest;
import co.istad.mbanking.features.account.dto.UpdateAccountRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateAccountRequestPartially(UpdateAccountRequest updateAccountRequest,
                                           @MappingTarget Account account);

    AccountDetailResponse toAccountDetailResponse(Account account);

    Account fromCreateAccountRequest(CreateAccountRequest createAccountRequest);

}
