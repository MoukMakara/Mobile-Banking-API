package co.istad.mbanking.features.card.dto;

public record CardResponse(Integer id,
                           String number,
                           String holder,
                           String cvv,
                           String pin,
                           String expiredAt,
                           Boolean isDeleted,
                           Integer cardTypeId,
                           String cardTypeName) {
}
