package co.istad.mbanking.features.card.dto;

public record CardRequest(
        String number,
        String holder,
        String cvv,
        String expiredAt,
        String cardTypeAlias
) {
}
