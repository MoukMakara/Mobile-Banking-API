package co.istad.mbanking.features.user;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.user.dto.CreateUserRequest;
import co.istad.mbanking.features.user.dto.UserResponse;
import co.istad.mbanking.features.user.dto.UserUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "user-controller")
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

    // UPDATE by UUID
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_STAFF', 'ROLE_CUSTOMER', 'USER')")
    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<UserResponse>> updateByUuid(@PathVariable String uuid,
                                                                  @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse userResponse = userService.updateByUuid(uuid, userUpdateRequest);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User updated successfully")
                .status(HttpStatus.OK)
                .payload(userResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // FIND LIST (PAGINATION)
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_STAFF')")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> findList(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int limit) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .message("Users fetched successfully")
                .status(HttpStatus.OK)
                .payload(userService.findList(page, limit))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // FIND BY UUID
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_STAFF')")
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<UserResponse>> findByUuid(@PathVariable String uuid) {
        UserResponse userResponse = userService.findByUuid(uuid);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User found successfully")
                .status(HttpStatus.OK)
                .payload(userResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // BLOCK USER BY UUID
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping("/{uuid}/block_and_unblock")
    public ResponseEntity<ApiResponse<?>> setBlockStatus(@PathVariable String uuid,
                                                         @RequestParam boolean status) {
        userService.setBlockAndUnBlockByUuid(uuid, status);
        String message = status ? "User has been blocked" : "User has been unblocked";
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .message(message)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // DELETE USER BY UUID
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<?>> deleteByUuid(@PathVariable String uuid) {
        userService.deleteByUuid(uuid);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .message("User has been deleted")
                .status(HttpStatus.NO_CONTENT)
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
    }

    // UPDATE PROFILE IMAGE
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_STAFF')")
    @PutMapping("/{uuid}/profile-image")
    public ResponseEntity<ApiResponse<String>> updateProfileImage(@PathVariable String uuid,
                                                                  @RequestParam("mediaName") String mediaName) {
        String imageUrl = userService.updateProfileImage(uuid, mediaName);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .success(true)
                .message("Profile image updated successfully")
                .status(HttpStatus.OK)
                .payload(imageUrl)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}
