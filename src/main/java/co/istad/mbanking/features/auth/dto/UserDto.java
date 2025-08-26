package co.istad.mbanking.features.auth.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record UserDto(
        String uuid,
        String name,
        String email,
        String profileImage,
        List<String> roles
) {
}
