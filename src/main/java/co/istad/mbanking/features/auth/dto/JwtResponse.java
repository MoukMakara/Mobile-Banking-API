package co.istad.mbanking.features.auth.dto;

import lombok.Builder;

@Builder
public record JwtResponse(
        UserDto user,
        String tokenType, // Bearer
        String accessToken,
        String refreshToken
) {
}
