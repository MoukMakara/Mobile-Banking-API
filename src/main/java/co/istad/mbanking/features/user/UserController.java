package co.istad.mbanking.features.user;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.user.dto.CreateUserRequest;
import co.istad.mbanking.features.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_STAFF')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createNew(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserResponse userResponse = userService.register(createUserRequest);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User created successfully")
                .status(HttpStatus.CREATED)
                .payload(userResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }



}
