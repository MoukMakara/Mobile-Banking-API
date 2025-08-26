package co.istad.mbanking.features.card.dto;

import co.istad.mbanking.features.account.dto.AccountDetailResponse;
import java.util.List;

public record CardResponse(Integer id,
                           String number,
                           String holder,
                           String cvv,
                           String expiredAt,
                           Boolean isDeleted,
//                           Integer cardTypeId,
//                           String cardTypeName,
//                           String cardTypeAlias,
                           CardTypeResponse cardType,
                           List<AccountDetailResponse> account) {
}
