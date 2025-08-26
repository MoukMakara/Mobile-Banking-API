package co.istad.mbanking.features.card;

import co.istad.mbanking.domain.Card;
import co.istad.mbanking.domain.CardType;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.card.dto.CardRequest;
import co.istad.mbanking.features.card.dto.CardResponse;
import co.istad.mbanking.features.card.dto.CardTypeResponse;
import co.istad.mbanking.mapper.CardMapper;
import co.istad.mbanking.security.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;
    private final CurrentUserUtil currentUserUtil;
//    private final AccountService accountService;

    // Helper method to generate a random card number
    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        // Most card numbers start with specific digits based on the card network
        // For simplicity, we'll use 4 (like Visa)
        cardNumber.append("4");

        // Generate 15 more random digits
        for (int i = 0; i < 15; i++) {
            cardNumber.append(random.nextInt(10));
        }

        // Check if this number already exists
        if (cardRepository.existsByNumber(cardNumber.toString())) {
            // If it exists, recursively try again
            return generateCardNumber();
        }

        return cardNumber.toString();
    }

    // Helper method to generate a random 3-digit CVV
    private String generateCVV() {
        Random random = new Random();
        StringBuilder cvv = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            cvv.append(random.nextInt(10));
        }

        return cvv.toString();
    }

    // Helper method to generate an expiry date (typically 3-5 years in the future)
    private LocalDate generateExpiryDate() {
        // Set expiry to 4 years from now
        return LocalDate.now().plusYears(4);
    }

    @Override
    public CardResponse createCard(CardRequest cardRequest) {
        // Check if card type exists
        CardType cardType = cardTypeRepository.findByAlias(cardRequest.cardTypeAlias())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Card type with alias " + cardRequest.cardTypeAlias() + " not found"));

        // Create new card
        Card card = new Card();
        card.setHolder(cardRequest.holder());

        // Auto-generate card number, CVV, and expiry date
        String cardNumber = generateCardNumber();
        String cvv = generateCVV();
        LocalDate expiryDate = generateExpiryDate();

        // Set generated and additional fields
        card.setNumber(cardNumber);
        card.setCvv(cvv);
        card.setCardType(cardType);
        card.setIssuedAt(LocalDate.now());
        card.setExpiredAt(expiryDate);
        card.setIsDeleted(false);

        // Get current user and set as the owner of the card
        User currentUser = currentUserUtil.getCurrentUser();
        card.setUser(currentUser);

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

        // Update the card holder if provided
        if (cardRequest.holder() != null && !cardRequest.holder().isBlank()) {
            card.setHolder(cardRequest.holder());
        }

        // Check card type and update if provided
        if (cardRequest.cardTypeAlias() != null && !cardRequest.cardTypeAlias().isBlank()) {
            CardType cardType = cardTypeRepository.findByAlias(cardRequest.cardTypeAlias())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Card type with alias " + cardRequest.cardTypeAlias() + " not found"));
            card.setCardType(cardType);
        }

        // Auto-generate new card details (similar to createCard)
        String cardNumber = generateCardNumber();
        String cvv = generateCVV();
        LocalDate expiryDate = generateExpiryDate();

        // Set the new auto-generated values
        card.setNumber(cardNumber);
        card.setCvv(cvv);
        card.setExpiredAt(expiryDate);
        // Reset the issue date to current date when updating
        card.setIssuedAt(LocalDate.now());

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

    @Override
    public List<CardResponse> getCurrentUserCards() {
        // Get the current user
        User currentUser = currentUserUtil.getCurrentUser();

        // Find all cards owned by this user
        List<Card> userCards = cardRepository.findByUserId(currentUser.getId());

        // Map to response DTOs and return
        return userCards.stream()
                .map(this::enrichCardResponseWithCardType)
                .collect(Collectors.toList());
    }
}
