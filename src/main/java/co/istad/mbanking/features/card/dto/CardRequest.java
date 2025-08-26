package co.istad.mbanking.features.card.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for creating a new card")
public record CardRequest(
        @NotBlank(message = "Holder name is required")
        @Schema(description = "Card holder name", required = true, example = "John Doe")
        String holder,

        @NotBlank(message = "Card type alias is required")
        @Schema(description = "Card type alias (e.g., visa-card)", required = true, example = "visa-card")
        String cardTypeAlias
) {
}
