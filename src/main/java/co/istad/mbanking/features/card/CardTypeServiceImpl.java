package co.istad.mbanking.features.card;

import co.istad.mbanking.domain.CardType;
import co.istad.mbanking.features.card.dto.CardTypeRequest;
import co.istad.mbanking.features.card.dto.CardTypeResponse;
import co.istad.mbanking.mapper.CardTypeMapper;
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
    private final CardTypeMapper cardTypeMapper;

    @Override
    public CardTypeResponse createCardType(CardTypeRequest cardTypeRequest) {
        // Check if card type with the same alias already exists
        if (cardTypeRepository.existsByAlias(cardTypeRequest.alias())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Card type with alias '" + cardTypeRequest.alias() + "' already exists"
            );
        }

        // Check if card type with the same name already exists
        if (cardTypeRepository.existsByName(cardTypeRequest.name())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Card type with name '" + cardTypeRequest.name() + "' already exists"
            );
        }

        // Create new card type
        CardType cardType = cardTypeMapper.fromCardTypeRequest(cardTypeRequest);
        cardType.setIsDeleted(false);

        // Save and return
        cardType = cardTypeRepository.save(cardType);
        return cardTypeMapper.toCardTypeResponse(cardType);
    }

    @Override
    public CardTypeResponse updateCardType(Integer id, CardTypeRequest cardTypeRequest) {
        // Find card type by ID
        CardType cardType = cardTypeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Card type with ID " + id + " not found"
            ));

        // Check if alias is changed and already exists
        if (cardTypeRequest.alias() != null &&
            !cardTypeRequest.alias().equals(cardType.getAlias()) &&
            cardTypeRepository.existsByAlias(cardTypeRequest.alias())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Card type with alias '" + cardTypeRequest.alias() + "' already exists"
            );
        }

        // Check if name is changed and already exists
        if (cardTypeRequest.name() != null &&
            !cardTypeRequest.name().equals(cardType.getName()) &&
            cardTypeRepository.existsByName(cardTypeRequest.name())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Card type with name '" + cardTypeRequest.name() + "' already exists"
            );
        }

        // Update card type fields (ignoring nulls)
        cardTypeMapper.updateCardTypeFromRequest(cardTypeRequest, cardType);

        // Save and return
        cardType = cardTypeRepository.save(cardType);
        return cardTypeMapper.toCardTypeResponse(cardType);
    }

    @Override
    public CardTypeResponse getCardTypeById(Integer id) {
        CardType cardType = cardTypeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Card type with ID " + id + " not found"
            ));

        return cardTypeMapper.toCardTypeResponse(cardType);
    }

    @Override
    public CardTypeResponse deleteCardTypeById(Integer id) {
        CardType cardType = cardTypeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Card type with ID " + id + " not found"
            ));

        // Soft delete
        cardType.setIsDeleted(true);
        cardType = cardTypeRepository.save(cardType);

        return cardTypeMapper.toCardTypeResponse(cardType);
    }

    @Override
    public List<CardTypeResponse> getAllActiveCardTypes() {
        List<CardType> activeCardTypes = cardTypeRepository.findByIsDeletedFalse();
        return activeCardTypes.stream()
                .map(cardTypeMapper::toCardTypeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardTypeResponse> getAllCardTypes() {
        List<CardType> allCardTypes = cardTypeRepository.findAll();
        return allCardTypes.stream()
                .map(cardTypeMapper::toCardTypeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CardTypeResponse getCardTypeByAlias(String alias) {
        CardType cardType = cardTypeRepository.findByAlias(alias)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Card type with alias '" + alias + "' not found"
            ));

        return cardTypeMapper.toCardTypeResponse(cardType);
    }
}
