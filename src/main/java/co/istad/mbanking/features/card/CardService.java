package co.istad.mbanking.features.card;

import co.istad.mbanking.features.card.dto.CardRequest;
import co.istad.mbanking.features.card.dto.CardResponse;

import java.util.List;

public interface CardService {
    CardResponse createCard(CardRequest cardRequest);

    CardResponse updateCard(Integer id, CardRequest cardRequest);

    CardResponse getCardById(Integer id);

    CardResponse deleteCardById(Integer id);

    CardResponse getCardByNumber(String number);

    List<CardResponse> getCardsByHolder(String holder);

    List<CardResponse> getAllActiveCards();

    List<CardResponse> getCardsByCardTypeId(Integer cardTypeId);

    List<CardResponse> getExpiredCards();

    List<CardResponse> getAllCards();
}