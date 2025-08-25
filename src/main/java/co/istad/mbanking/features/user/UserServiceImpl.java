package co.istad.mbanking.features.user;

import co.istad.mbanking.domain.Role;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.auth.RoleRepository;
import co.istad.mbanking.features.auth.dto.ChangePasswordRequest;
import co.istad.mbanking.features.auth.dto.ResetPasswordRequest;
import co.istad.mbanking.features.user.dto.CreateUserRequest;
import co.istad.mbanking.features.user.dto.UserResponse;
import co.istad.mbanking.features.user.dto.UserUpdateRequest;
import co.istad.mbanking.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${file-server.base-uri}")
    private String fileServerBaseUri;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Override
    public UserResponse register(CreateUserRequest createUserRequest) {

        // Validate national Card ID
        if (userRepository.isNationalCardIdExisted(createUserRequest.nationalCardId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "National card ID already exists");
        }

        // Validate phone number
        if (userRepository.existsByPhoneNumber(createUserRequest.phoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Phone number already exists");
        }

        // Validate email
        if (userRepository.existsByEmail(createUserRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email already exists");
        }

        // Validate password and confirmed password
        if (!createUserRequest.password().equals(createUserRequest.confirmedPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Passwords do not match");
        }

        // Transfer data from DTO to Domain Model
        User user = userMapper.fromCreateUserRequest(createUserRequest);
        user.setUuid(UUID.randomUUID().toString());
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsVerified(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setProfileImage("user-avatar.png");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(1).orElseThrow());
        roles.add(roleRepository.findById(2).orElseThrow());
        user.setRoles(roles);

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateByUuid(String uuid, UserUpdateRequest userUpdateRequest) {

        // check uuid if exists
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User has not been found!"));

        log.info("before user: {}", user);

        userMapper.fromUserUpdateRequest(userUpdateRequest, user);


        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public Page<UserResponse> findList(int page, int limit) {
        // Create pageRequest object
        PageRequest pageRequest = PageRequest.of(page, limit);
        // Invoke findAll(pageRequest)
        Page<User> users = userRepository.findAll(pageRequest);
        // Map result of pagination to response
        return users.map(userMapper::toUserResponse);

    }

    @Override
    public UserResponse findByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User has not been found!"));

        return userMapper.toUserResponse(user);

    }

    @Override
    public void setBlockAndUnBlockByUuid(String uuid, boolean status) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found!"));

        user.setIsBlocked(status);
        userRepository.save(user);
    }

    @Override
    public void deleteByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User has not been found!"));
        userRepository.delete(user);
    }

    @Override
    public String updateProfileImage(String uuid, String mediaName) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User has not been found!"));

        user.setProfileImage(mediaName);

        userRepository.save(user);

        return fileServerBaseUri + user.getProfileImage();
    }

    @Override
    public UserResponse updateRoleByUuid(String uuid, String roleName) {
        // Find user by UUID
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User has not been found!"));

        // Find role by name
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Role has not been found!"));

        // Check if user already has this role
        boolean hasRole = user.getRoles().stream()
                .anyMatch(r -> r.getName().equals(roleName));

        if (hasRole) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User already has role: " + roleName);
        }

        // Add the role to user's roles
        user.getRoles().add(role);
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public void changePassword(String uuid, ChangePasswordRequest changePasswordRequest) {

        // Find user by UUID
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found!"
                ));

        // Check if old password matches
        if (!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        }

        // Validate new password and confirmed password match
        if (!changePasswordRequest.password().equals(changePasswordRequest.confirmedPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        }

        // Update the password securely
        user.setPassword(passwordEncoder.encode(changePasswordRequest.password()));
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", user.getEmail());
    }

    /**
     * Reset Password API
     */
    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {

        // Find user by email
        User user = userRepository.findByEmail(resetPasswordRequest.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with this email has not been found!"
                ));

        // Generate a secure temporary password
        String temporaryPassword = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10);

        // Update user's password
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);

        // Send email with the temporary password
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom(mailUsername);
            mailMessage.setSubject("Your Password Reset Request");
            mailMessage.setText(
                    "Hello " + user.getName() + ",\n\n" +
                            "We received a request to reset your password.\n" +
                            "Here is your temporary password: " + temporaryPassword + "\n\n" +
                            "⚠ Please log in and change your password immediately for security.\n\n" +
                            "Thank you,\nBanking API Team"
            );

            mailSender.send(mailMessage);

            log.info("Temporary password sent successfully to: {}", user.getEmail());

        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to send temporary password. Please try again.");
        }
    }
}
