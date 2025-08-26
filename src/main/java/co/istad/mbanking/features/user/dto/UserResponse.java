package co.istad.mbanking.features.user.dto;

import java.time.LocalDate;
import java.util.List;

public record UserResponse(
        String uuid,
        String name,
        String gender,
        LocalDate dob,
        String nationalCardId,
        String phoneNumber,
        String email,
        String profileImage,
        Boolean isVerified,
        Boolean isBlocked,
        List<String> roles
) {
}
