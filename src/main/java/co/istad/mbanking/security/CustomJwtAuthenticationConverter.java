package co.istad.mbanking.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

//@Component
//public class CustomJwtAuthenticationConverter implements Converter<Jwt, CustomUserDetails> {
//    @Override
//    public CustomUserDetails convert(Jwt source) {
//        System.out.println(source.getTokenValue());
//        CustomUserDetails customUserDetails = new CustomUserDetails();
//        return
//    }
//}
