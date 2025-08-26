package co.istad.mbanking.features.card;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.card.dto.CardTypeRequest;
import co.istad.mbanking.features.card.dto.CardTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/card-types")
@RequiredArgsConstructor
public class CardTypeController {

    private final CardTypeService cardTypeService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CardTypeResponse>> createCardType(@Valid @RequestBody CardTypeRequest cardTypeRequest) {
        CardTypeResponse response = cardTypeService.createCardType(cardTypeRequest);

        ApiResponse<CardTypeResponse> apiResponse = ApiResponse.<CardTypeResponse>builder()
                .success(true)
                .message("Card type created successfully")
                .status(HttpStatus.CREATED)
                .payload(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_ADMIN')")
    @PutMapping("/{alias}")
    public ResponseEntity<ApiResponse<CardTypeResponse>> updateCardType(@PathVariable String alias,
                                                                      @Valid @RequestBody CardTypeRequest cardTypeRequest) {
        CardTypeResponse response = cardTypeService.updateCardType(alias, cardTypeRequest);

        ApiResponse<CardTypeResponse> apiResponse = ApiResponse.<CardTypeResponse>builder()
                .success(true)
                .message("Card type updated successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{alias}")
    public ResponseEntity<ApiResponse<CardTypeResponse>> getCardTypeByAlias(@PathVariable String alias) {
        CardTypeResponse response = cardTypeService.getCardTypeByAlias(alias);

        ApiResponse<CardTypeResponse> apiResponse = ApiResponse.<CardTypeResponse>builder()
                .success(true)
                .message("Card type retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_ADMIN')")
    @DeleteMapping("/{alias}")
    public ResponseEntity<ApiResponse<Void>> deleteCardType(@PathVariable String alias) {
        cardTypeService.deleteCardType(alias);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(true)
                .message("Card type deleted successfully")
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CardTypeResponse>>> getAllCardTypes() {
        List<CardTypeResponse> response = cardTypeService.getAllCardTypes();

        ApiResponse<List<CardTypeResponse>> apiResponse = ApiResponse.<List<CardTypeResponse>>builder()
                .success(true)
                .message("All card types retrieved")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
