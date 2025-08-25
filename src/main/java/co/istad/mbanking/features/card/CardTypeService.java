package co.istad.mbanking.features.card;

import co.istad.mbanking.features.card.dto.CardTypeRequest;
import co.istad.mbanking.features.card.dto.CardTypeResponse;

import java.util.List;

public interface CardTypeService {
    // Create a new card type
    CardTypeResponse createCardType(CardTypeRequest cardTypeRequest);

    // Update an existing card type
    CardTypeResponse updateCardType(Integer id, CardTypeRequest cardTypeRequest);

    // Get card type by ID
    CardTypeResponse getCardTypeById(Integer id);

    // Delete card type (soft delete)
    CardTypeResponse deleteCardTypeById(Integer id);

    // Get all active card types
    List<CardTypeResponse> getAllActiveCardTypes();

    // Get all card types (including deleted)
    List<CardTypeResponse> getAllCardTypes();

    // Get card type by alias
    CardTypeResponse getCardTypeByAlias(String alias);
}
