package co.istad.mbanking.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            String id = jwt.getId();
            log.info("ID: {}", id);
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(id);
            log.info("AUTHORITIES: {}", userDetails.getAuthorities());
            return userDetails.getAuthorities()
                    .stream()
                    .map(grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority()))
                    .collect(Collectors.toList());
        };

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider(@Qualifier("jwtDecoderRefreshToken") JwtDecoder jwtDecoderRefreshToken) {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoderRefreshToken);
        provider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
        return provider;
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

    @Bean
    SecurityFilterChain configFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {

        // Protect routes
        http.authorizeHttpRequests(endpoint -> endpoint
                .requestMatchers("/api/v1/auth/**",
                        "http://localhost:8080/api/v1/auth/verify",
                        "/api/v1/upload/**",
                        "/upload/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html")
                .permitAll()
                .requestMatchers("/api/v1/files/**").permitAll()  // Allow all methods on file endpoints without authentication
                .requestMatchers(HttpMethod.GET, "/api/v1/card-types").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/account-types").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users").hasAnyRole("MANAGER", "ADMIN")
                .anyRequest().authenticated()
        );


        /*http.authorizeHttpRequests(endpoint -> endpoint
                .anyRequest().authenticated());*/

        // Security Mechanism
        // http.httpBasic(Customizer.withDefaults());
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder))
        );

        // Disable CSRF Token
        http.csrf(token -> token.disable());

        // Make API stateless
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
