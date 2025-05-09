package co.istad.mbanking.features.auth;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.auth.dto.*;
import co.istad.mbanking.features.user.dto.CreateUserRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtResponse jwtResponse = authService.refreshToken(refreshTokenRequest);
        ApiResponse<JwtResponse> response = ApiResponse.<JwtResponse>builder()
                .success(true)
                .message("Token refreshed successfully")
                .status(HttpStatus.OK)
                .payload(jwtResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        JwtResponse jwt = authService.login(loginRequest);
        ApiResponse<JwtResponse> response = ApiResponse.<JwtResponse>builder()
                .success(true)
                .message("Login successful")
                .status(HttpStatus.OK)
                .payload(jwt)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verify(@Valid @RequestBody VerifyRequest verifyRequest) {
        authService.verify(verifyRequest);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Verification successful")
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-verify")
    public ResponseEntity<ApiResponse<Void>> resendVerification(@Valid @RequestBody ReVerifyRequest reVerifyRequest) throws MessagingException {
        authService.resendVerification(reVerifyRequest.email());
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Verification email resent")
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException {
        authService.register(registerRequest);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("User registered successfully. Check your email for OTP.")
                .status(HttpStatus.CREATED)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
