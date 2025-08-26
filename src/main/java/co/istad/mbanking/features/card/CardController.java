package co.istad.mbanking.features.card;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.card.dto.CardRequest;
import co.istad.mbanking.features.card.dto.CardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<ApiResponse<CardResponse>> createCard(@Valid @RequestBody CardRequest cardRequest) {
        CardResponse response = cardService.createCard(cardRequest);

        ApiResponse<CardResponse> apiResponse = ApiResponse.<CardResponse>builder()
                .success(true)
                .message("Card created successfully")
                .status(HttpStatus.CREATED)
                .payload(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CardResponse>> updateCard(@PathVariable Integer id,
                                                                @Valid @RequestBody CardRequest cardRequest) {
        CardResponse response = cardService.updateCard(id, cardRequest);

        ApiResponse<CardResponse> apiResponse = ApiResponse.<CardResponse>builder()
                .success(true)
                .message("Card updated successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CardResponse>> deleteCard(@PathVariable Integer id) {
        CardResponse response = cardService.deleteCardById(id);

        ApiResponse<CardResponse> apiResponse = ApiResponse.<CardResponse>builder()
                .success(true)
                .message("Card deleted successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CardResponse>> getCardById(@PathVariable Integer id) {
        CardResponse response = cardService.getCardById(id);

        ApiResponse<CardResponse> apiResponse = ApiResponse.<CardResponse>builder()
                .success(true)
                .message("Card retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/number/{number}")
    public ResponseEntity<ApiResponse<CardResponse>> getCardByNumber(@PathVariable String number) {
        CardResponse response = cardService.getCardByNumber(number);

        ApiResponse<CardResponse> apiResponse = ApiResponse.<CardResponse>builder()
                .success(true)
                .message("Card retrieved successfully by number")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/holder/{holder}")
    public ResponseEntity<ApiResponse<List<CardResponse>>> getCardsByHolder(@PathVariable String holder) {
        List<CardResponse> response = cardService.getCardsByHolder(holder);

        ApiResponse<List<CardResponse>> apiResponse = ApiResponse.<List<CardResponse>>builder()
                .success(true)
                .message("Cards retrieved successfully by holder")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_CUSTOMER')")
    @GetMapping("/my-cards")
    public ResponseEntity<ApiResponse<List<CardResponse>>> getCurrentUserCards() {
        List<CardResponse> response = cardService.getCurrentUserCards();

        ApiResponse<List<CardResponse>> apiResponse = ApiResponse.<List<CardResponse>>builder()
                .success(true)
                .message("Your cards retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CardResponse>>> getAllActiveCards() {
        List<CardResponse> response = cardService.getAllActiveCards();

        ApiResponse<List<CardResponse>> apiResponse = ApiResponse.<List<CardResponse>>builder()
                .success(true)
                .message("Active cards retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/type/{cardTypeId}")
    public ResponseEntity<ApiResponse<List<CardResponse>>> getCardsByCardType(@PathVariable Integer cardTypeId) {
        List<CardResponse> response = cardService.getCardsByCardTypeId(cardTypeId);

        ApiResponse<List<CardResponse>> apiResponse = ApiResponse.<List<CardResponse>>builder()
                .success(true)
                .message("Cards retrieved successfully by type")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/expired")
    public ResponseEntity<ApiResponse<List<CardResponse>>> getExpiredCards() {
        List<CardResponse> response = cardService.getExpiredCards();

        ApiResponse<List<CardResponse>> apiResponse = ApiResponse.<List<CardResponse>>builder()
                .success(true)
                .message("Expired cards retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_ADMIN')")
    @GetMapping("/all-cards")
    public ResponseEntity<ApiResponse<List<CardResponse>>> getAllCards() {
        List<CardResponse> response = cardService.getAllCards();

        ApiResponse<List<CardResponse>> apiResponse = ApiResponse.<List<CardResponse>>builder()
                .success(true)
                .message("All cards retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}