package co.istad.mbanking.features.account.dto;

import java.math.BigDecimal;

public record UpdateAccountRequest(
        String aliasName,
        BigDecimal transferLimit,
        Boolean isHidden
) {
}
