package co.istad.mbanking.features.card;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.card.dto.CardTypeRequest;
import co.istad.mbanking.features.card.dto.CardTypeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CardTypeResponse>> updateCardType(
            @PathVariable Integer id,
            @Valid @RequestBody CardTypeRequest cardTypeRequest) {
        CardTypeResponse response = cardTypeService.updateCardType(id, cardTypeRequest);

        ApiResponse<CardTypeResponse> apiResponse = ApiResponse.<CardTypeResponse>builder()
                .success(true)
                .message("Card type updated successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CardTypeResponse>> getCardTypeById(@PathVariable Integer id) {
        CardTypeResponse response = cardTypeService.getCardTypeById(id);

        ApiResponse<CardTypeResponse> apiResponse = ApiResponse.<CardTypeResponse>builder()
                .success(true)
                .message("Card type retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/alias/{alias}")
    public ResponseEntity<ApiResponse<CardTypeResponse>> getCardTypeByAlias(@PathVariable String alias) {
        CardTypeResponse response = cardTypeService.getCardTypeByAlias(alias);

        ApiResponse<CardTypeResponse> apiResponse = ApiResponse.<CardTypeResponse>builder()
                .success(true)
                .message("Card type retrieved successfully by alias")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CardTypeResponse>>> getAllActiveCardTypes() {
        List<CardTypeResponse> response = cardTypeService.getAllActiveCardTypes();

        ApiResponse<List<CardTypeResponse>> apiResponse = ApiResponse.<List<CardTypeResponse>>builder()
                .success(true)
                .message("Active card types retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CardTypeResponse>>> getAllCardTypes() {
        List<CardTypeResponse> response = cardTypeService.getAllCardTypes();

        ApiResponse<List<CardTypeResponse>> apiResponse = ApiResponse.<List<CardTypeResponse>>builder()
                .success(true)
                .message("All card types retrieved successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CardTypeResponse>> deleteCardType(@PathVariable Integer id) {
        CardTypeResponse response = cardTypeService.deleteCardTypeById(id);

        ApiResponse<CardTypeResponse> apiResponse = ApiResponse.<CardTypeResponse>builder()
                .success(true)
                .message("Card type deleted successfully")
                .status(HttpStatus.OK)
                .payload(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
