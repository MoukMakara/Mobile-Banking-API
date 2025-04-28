package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.features.account.dto.AccountTypeRequest;
import co.istad.mbanking.features.account.dto.AccountTypeResponse;
import co.istad.mbanking.features.account.dto.AccountTypeUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountTypeMapper {

    List<AccountTypeResponse> toAccountTypeResponseList(List<AccountType> accountTypes);

    AccountTypeResponse toAccountTypeResponse(AccountType accountType);

    AccountType fromAccountTypeRequest(AccountTypeRequest accountTypeRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromAccountTypeUpdateRequest(AccountTypeUpdateRequest accountTypeUpdateRequest,
                                      @MappingTarget AccountType accountType);
}
