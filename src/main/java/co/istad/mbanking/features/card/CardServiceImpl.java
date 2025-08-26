package co.istad.mbanking.features.card;

import co.istad.mbanking.domain.Card;
import co.istad.mbanking.domain.CardType;
import co.istad.mbanking.features.card.dto.CardRequest;
import co.istad.mbanking.features.card.dto.CardResponse;
import co.istad.mbanking.features.card.dto.CardTypeResponse;
import co.istad.mbanking.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;


    @Override
    public CardResponse createCard(CardRequest cardRequest) {
        // Check if card number already exists
        if (cardRepository.existsByNumber(cardRequest.number())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card number already exists");
        }

        // Check if card type exists
        CardType cardType = cardTypeRepository.findByAlias(cardRequest.cardTypeAlias())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Card type with alias " + cardRequest.cardTypeAlias() + " not found"));

        // Create new card
        Card card = cardMapper.fromCardRequest(cardRequest);

        // Set additional fields
        card.setCardType(cardType);
        card.setIssuedAt(LocalDate.now());
        card.setExpiredAt(LocalDate.parse(cardRequest.expiredAt(), DateTimeFormatter.ISO_DATE));
        card.setIsDeleted(false);

        // Save and return
        card = cardRepository.save(card);
        CardResponse cardResponse = cardMapper.toCardResponse(card);

        // Add card type details
        CardTypeResponse cardTypeResponse = cardMapper.toCardTypeResponse(cardType);

        return new CardResponse(
            cardResponse.id(),
            cardResponse.number(),
            cardResponse.holder(),
            cardResponse.cvv(),
            cardResponse.expiredAt(),
            cardResponse.isDeleted(),
            cardResponse.cardTypeId(),
            cardResponse.cardTypeName(),
            cardResponse.cardTypeAlias(),
            cardTypeResponse
        );
    }

    @Override
    public CardResponse updateCard(Integer id, CardRequest cardRequest) {
        // Find card by ID
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Card with ID " + id + " not found"));

        // Check if number is changed and already exists
        if (cardRequest.number() != null &&
                !cardRequest.number().equals(card.getNumber()) &&
                cardRepository.existsByNumber(cardRequest.number())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card number already exists");
        }

        // Check card type if provided
        if (cardRequest.cardTypeAlias() != null) {
            CardType cardType = cardTypeRepository.findByAlias(cardRequest.cardTypeAlias())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Card type with alias " + cardRequest.cardTypeAlias() + " not found"));
            card.setCardType(cardType);
        }

        // Update card fields (ignoring nulls)
        cardMapper.updateCardFromRequest(cardRequest, card);

        // Update expired date if provided
        if (cardRequest.expiredAt() != null) {
            card.setExpiredAt(LocalDate.parse(cardRequest.expiredAt(), DateTimeFormatter.ISO_DATE));
        }

        // Save and return
        card = cardRepository.save(card);
        CardResponse cardResponse = cardMapper.toCardResponse(card);

        // Add card type details
        CardTypeResponse cardTypeResponse = cardMapper.toCardTypeResponse(card.getCardType());

        return new CardResponse(
            cardResponse.id(),
            cardResponse.number(),
            cardResponse.holder(),
            cardResponse.cvv(),
            cardResponse.expiredAt(),
            cardResponse.isDeleted(),
            cardResponse.cardTypeId(),
            cardResponse.cardTypeName(),
            cardResponse.cardTypeAlias(),
            cardTypeResponse
        );
    }

    @Override
    public CardResponse getCardById(Integer id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Card with ID " + id + " not found"));

        CardResponse cardResponse = cardMapper.toCardResponse(card);
        CardTypeResponse cardTypeResponse = cardMapper.toCardTypeResponse(card.getCardType());

        return new CardResponse(
            cardResponse.id(),
            cardResponse.number(),
            cardResponse.holder(),
            cardResponse.cvv(),
            cardResponse.expiredAt(),
            cardResponse.isDeleted(),
            cardResponse.cardTypeId(),
            cardResponse.cardTypeName(),
            cardResponse.cardTypeAlias(),
            cardTypeResponse
        );
    }

    @Override
    public CardResponse deleteCardById(Integer id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Card with ID " + id + " not found"));

        // Soft delete
        card.setIsDeleted(true);
        card = cardRepository.save(card);

        CardResponse cardResponse = cardMapper.toCardResponse(card);
        CardTypeResponse cardTypeResponse = cardMapper.toCardTypeResponse(card.getCardType());

        return new CardResponse(
            cardResponse.id(),
            cardResponse.number(),
            cardResponse.holder(),
            cardResponse.cvv(),
            cardResponse.expiredAt(),
            cardResponse.isDeleted(),
            cardResponse.cardTypeId(),
            cardResponse.cardTypeName(),
            cardResponse.cardTypeAlias(),
            cardTypeResponse
        );
    }

    @Override
    public CardResponse getCardByNumber(String number) {
        Card card = cardRepository.findByNumber(number)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Card with number " + number + " not found"));

        CardResponse cardResponse = cardMapper.toCardResponse(card);
        CardTypeResponse cardTypeResponse = cardMapper.toCardTypeResponse(card.getCardType());

        return new CardResponse(
            cardResponse.id(),
            cardResponse.number(),
            cardResponse.holder(),
            cardResponse.cvv(),
            cardResponse.expiredAt(),
            cardResponse.isDeleted(),
            cardResponse.cardTypeId(),
            cardResponse.cardTypeName(),
            cardResponse.cardTypeAlias(),
            cardTypeResponse
        );
    }

    private CardResponse enrichCardResponseWithCardType(Card card) {
        CardResponse cardResponse = cardMapper.toCardResponse(card);
        CardTypeResponse cardTypeResponse = cardMapper.toCardTypeResponse(card.getCardType());

        return new CardResponse(
            cardResponse.id(),
            cardResponse.number(),
            cardResponse.holder(),
            cardResponse.cvv(),
            cardResponse.expiredAt(),
            cardResponse.isDeleted(),
            cardResponse.cardTypeId(),
            cardResponse.cardTypeName(),
            cardResponse.cardTypeAlias(),
            cardTypeResponse
        );
    }

    @Override
    public List<CardResponse> getCardsByHolder(String holder) {
        List<Card> cards = cardRepository.findByHolder(holder);
        return cards.stream()
                .map(this::enrichCardResponseWithCardType)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardResponse> getAllActiveCards() {
        List<Card> activeCards = cardRepository.findByIsDeletedFalse();
        return activeCards.stream()
                .map(this::enrichCardResponseWithCardType)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardResponse> getCardsByCardTypeId(Integer cardTypeId) {
        // Verify card type exists
        if (!cardTypeRepository.existsById(cardTypeId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Card type with ID " + cardTypeId + " not found");
        }

        List<Card> cards = cardRepository.findByCardTypeId(cardTypeId);
        return cards.stream()
                .map(this::enrichCardResponseWithCardType)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardResponse> getExpiredCards() {
        List<Card> expiredCards = cardRepository.findByExpiredAtBefore(LocalDate.now());
        return expiredCards.stream()
                .map(this::enrichCardResponseWithCardType)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardResponse> getAllCards() {
        List<Card> validCards = cardRepository.findByExpiredAtAfter(LocalDate.now());
        return validCards.stream()
                .map(this::enrichCardResponseWithCardType)
                .collect(Collectors.toList());
    }
}
