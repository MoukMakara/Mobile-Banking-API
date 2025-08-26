package co.istad.mbanking.features.card;

import co.istad.mbanking.features.card.dto.CardTypeRequest;
import co.istad.mbanking.features.card.dto.CardTypeResponse;

import java.util.List;

public interface CardTypeService {
    CardTypeResponse createCardType(CardTypeRequest cardTypeRequest);
    CardTypeResponse updateCardType(String alias, CardTypeRequest cardTypeRequest);
    CardTypeResponse getCardTypeByAlias(String alias);
    void deleteCardType(String alias);
    List<CardTypeResponse> getAllCardTypes();
}
