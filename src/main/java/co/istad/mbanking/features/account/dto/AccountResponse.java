package co.istad.mbanking.features.account.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountResponse(
        String alias,
        String actName,
        String actNo,
        BigDecimal balance,
        Boolean isDeleted,
        AccountTypeResponse accountType
) {
}