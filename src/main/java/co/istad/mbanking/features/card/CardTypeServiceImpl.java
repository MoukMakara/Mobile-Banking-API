package co.istad.mbanking.features.card;

import co.istad.mbanking.domain.CardType;
import co.istad.mbanking.features.card.dto.CardTypeRequest;
import co.istad.mbanking.features.card.dto.CardTypeResponse;
import co.istad.mbanking.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardTypeServiceImpl implements CardTypeService {

    private final CardTypeRepository cardTypeRepository;
    private final CardMapper cardMapper;

    @Override
    public CardTypeResponse createCardType(CardTypeRequest cardTypeRequest) {
        // Check if card type with the same alias already exists
        if (cardTypeRepository.existsByAlias(cardTypeRequest.alias())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Card type with alias " + cardTypeRequest.alias() + " already exists"
            );
        }

        // Create new card type
        CardType cardType = new CardType();
        cardType.setAlias(cardTypeRequest.alias());
        cardType.setName(cardTypeRequest.name());
        cardType.setIsDeleted(false);

        // Save and return
        cardType = cardTypeRepository.save(cardType);
        return cardMapper.toCardTypeResponse(cardType);
    }

    @Override
    public CardTypeResponse updateCardType(String alias, CardTypeRequest cardTypeRequest) {
        // Find card type by alias
        CardType cardType = cardTypeRepository.findByAlias(alias)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Card type with alias " + alias + " not found"
                ));

        // Check if new alias conflicts with existing ones
        if (!cardTypeRequest.alias().equals(alias) &&
                cardTypeRepository.existsByAlias(cardTypeRequest.alias())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Card type with alias " + cardTypeRequest.alias() + " already exists"
            );
        }

        // Update fields
        cardType.setAlias(cardTypeRequest.alias());
        cardType.setName(cardTypeRequest.name());

        // Save and return
        cardType = cardTypeRepository.save(cardType);
        return cardMapper.toCardTypeResponse(cardType);
    }

    @Override
    public CardTypeResponse getCardTypeByAlias(String alias) {
        CardType cardType = cardTypeRepository.findByAlias(alias)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Card type with alias " + alias + " not found"
                ));

        return cardMapper.toCardTypeResponse(cardType);
    }

    @Override
    public void deleteCardType(String alias) {
        CardType cardType = cardTypeRepository.findByAlias(alias)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Card type with alias " + alias + " not found"
                ));

        // Soft delete
        cardType.setIsDeleted(true);
        cardTypeRepository.save(cardType);
    }

    @Override
    public List<CardTypeResponse> getAllCardTypes() {
        return cardTypeRepository.findAll().stream()
                .filter(cardType -> !cardType.getIsDeleted())
                .map(cardMapper::toCardTypeResponse)
                .collect(Collectors.toList());
    }
}
