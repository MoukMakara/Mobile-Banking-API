package co.istad.mbanking.features.auth;

import co.istad.mbanking.features.auth.dto.*;
import jakarta.mail.MessagingException;

public interface AuthService {

    JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    JwtResponse login(LoginRequest loginRequest);

    void verify(VerifyRequest verifyRequest);

    void resendVerification(String email) throws MessagingException;

    void register(RegisterRequest registerRequest) throws MessagingException;

}
