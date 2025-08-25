package co.istad.mbanking.security;

import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

/**
 * Utility service for retrieving current authenticated user
 */
@Service
@RequiredArgsConstructor
public class CurrentUserUtil {

    private final UserRepository userRepository;

    /**
     * Get the current authenticated user
     * @return the User entity for the authenticated user
     * @throws ResponseStatusException if no user is authenticated or user is not found
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        String userEmail;

        // Handle different authentication types
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            userEmail = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt) {
            userEmail = ((Jwt) authentication.getPrincipal()).getId();
        } else {
            userEmail = authentication.getName();
        }

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with email: " + userEmail));
    }

    /**
     * Get the UUID of the current authenticated user
     * @return the UUID string of the authenticated user
     */
    public String getCurrentUserUuid() {
        return getCurrentUser().getUuid();
    }

    /**
     * Check if the current user has any of the specified roles
     * @param roles the roles to check for
     * @return true if the user has any of the roles, false otherwise
     */
    public boolean hasAnyRole(String... roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        for (String role : roles) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals(role) ||
                              authority.getAuthority().equals("ROLE_" + role))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the current user is the owner of the specified resource by UUID
     * @param resourceUuid UUID of the resource to check
     * @return true if the user is the owner, false otherwise
     */
    public boolean isResourceOwner(String resourceUuid) {
        return getCurrentUserUuid().equals(resourceUuid);
    }
}
