package co.istad.mbanking.features.account.dto;

import java.math.BigDecimal;

public record AccountDetailResponse(
        String aliasName,
        String actNo,
        BigDecimal balance,
        BigDecimal transferLimit,
        Boolean isHidden
) {
}
