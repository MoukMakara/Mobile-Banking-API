package co.istad.mbanking.features.auth;

import co.istad.mbanking.domain.EmailVerification;
import co.istad.mbanking.domain.Role;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.auth.dto.*;
import co.istad.mbanking.features.user.UserRepository;
import co.istad.mbanking.mapper.UserMapper;
import co.istad.mbanking.security.CustomUserDetails;
import co.istad.mbanking.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String adminMail;

    private final String TOKEN_TYPE = "Bearer";

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final JwtEncoder jwtEncoder;
    private JwtEncoder jwtEncoderRefreshToken;

    @Autowired
    @Qualifier("jwtEncoderRefreshToken")
    public void setJwtEncoderRefreshToken(JwtEncoder jwtEncoderRefreshToken) {
        this.jwtEncoderRefreshToken = jwtEncoderRefreshToken;
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        Authentication auth = new BearerTokenAuthenticationToken(refreshTokenRequest.token());

        auth = jwtAuthenticationProvider.authenticate(auth);

        String scope = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        log.info("New Scope: {}", scope);
        log.info("Auth: {}", auth);

        Instant now = Instant.now();

        Jwt jwt = (Jwt) auth.getPrincipal();

        // Create access token claims set
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(jwt.getId())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nextjs", "reactjs"))
                .subject("Access Token")
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .claim("scope", scope)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwtClaimsSet);
        Jwt encodedJwt = jwtEncoder.encode(jwtEncoderParameters);

        String accessToken = encodedJwt.getTokenValue();
        String refreshToken = refreshTokenRequest.token();

        if (Duration.between(Instant.now(), jwt.getExpiresAt()).toDays() < 2) {
            // Create refresh token claims set
            JwtClaimsSet jwtClaimsSetRefreshToken = JwtClaimsSet.builder()
                    .id(auth.getName())
                    .issuedAt(now)
                    .issuer("web")
                    .audience(List.of("nextjs", "reactjs"))
                    .subject("Refresh Token")
                    .expiresAt(now.plus(7, ChronoUnit.DAYS))
                    .build();
            JwtEncoderParameters jwtEncoderParametersRefreshToken = JwtEncoderParameters.from(jwtClaimsSetRefreshToken);
            Jwt jwtRefreshToken = jwtEncoderRefreshToken.encode(jwtEncoderParametersRefreshToken);
            refreshToken = jwtRefreshToken.getTokenValue();
        }

        return JwtResponse.builder()
                .tokenType(TOKEN_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Override
    public JwtResponse login(LoginRequest loginRequest) {

        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        auth = daoAuthenticationProvider.authenticate(auth);

        /*CustomUserDetails customUserDetails = (CustomUserDetails) auth.getPrincipal();
        log.info(customUserDetails.getUser().getName());*/


        // Prepare SCOPE
        log.info("Authorities: {}", auth.getAuthorities());
        // ADMIN MANAGER USER
        String scope = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        log.info("SCOPE: {}", scope);

        Instant now = Instant.now();

        // Create access token claims set
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nextjs", "reactjs"))
                .subject("Access Token")
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .claim("scope", scope)
                .build();

        // Create refresh token claims set
        JwtClaimsSet jwtClaimsSetRefreshToken = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nextjs", "reactjs"))
                .subject("Refresh Token")
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwtClaimsSet);
        Jwt jwt = jwtEncoder.encode(jwtEncoderParameters);

        JwtEncoderParameters jwtEncoderParametersRefreshToken = JwtEncoderParameters.from(jwtClaimsSetRefreshToken);
        Jwt jwtRefreshToken = jwtEncoderRefreshToken.encode(jwtEncoderParametersRefreshToken);

        String accessToken = jwt.getTokenValue();
        String refreshToken = jwtRefreshToken.getTokenValue();

        return JwtResponse.builder()
                .tokenType(TOKEN_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Override
    public void verify(VerifyRequest verifyRequest) {

        User user = userRepository
                .findByEmail(verifyRequest.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Email doesn't exist"
                ));

        EmailVerification emailVerification = emailVerificationRepository
                .findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Verification failed"
                ));

        if (!emailVerification.getVerificationCode().equals(verifyRequest.verificationCode())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Verification code is invalid");
        }

        if (LocalDateTime.now().isAfter(emailVerification.getExpiryTime())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Verification code is expired");
        }


        user.setIsVerified(true);
        user.setIsDeleted(false);
        userRepository.save(user);
    }


    @Override
    public void register(RegisterRequest registerRequest) throws MessagingException {

        // Validate national Card ID
        if (userRepository.isNationalCardIdExisted(registerRequest.nationalCardId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "National card ID already exists");
        }

        // Validate phone number
        if (userRepository.existsByPhoneNumber(registerRequest.phoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Phone number already exists");
        }

        // Validate email
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email already exists");
        }

        // Validate password and confirmed password
        if (!registerRequest.password().equals(registerRequest.confirmedPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Passwords do not match");
        }

        // Transfer data from DTO to Domain Model
        User user = userMapper.fromRegisterRequest(registerRequest);
        user.setUuid(UUID.randomUUID().toString());
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsVerified(false);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setProfileImage("user-avatar.png");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(1).orElseThrow());
        roles.add(roleRepository.findById(2).orElseThrow());
        user.setRoles(roles);

        user = userRepository.save(user);

        // Send email for verification

        // Step 1. Prepare email verification data
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setVerificationCode(RandomUtil.random6Digits());
        emailVerification.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        emailVerification.setUser(user);

        emailVerificationRepository.save(emailVerification);

        // Step 2. Prepare to send mail

        String myHtml = String.format("""
                <h1>MBanking - Email Verification</h1>
                <hr/>
                %s
                """, emailVerification.getVerificationCode());

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Email Verification - MBanking");
        helper.setTo(user.getEmail());
        helper.setFrom(adminMail);
        helper.setText(myHtml, true);

        javaMailSender.send(mimeMessage);
    }

}
