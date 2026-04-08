package com.dp9v.lingua_learn_server.api.components;

import com.dp9v.lingua_learn_server.api.dto.auth.LoginResponse;
import com.dp9v.lingua_learn_server.api.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class JwtService {

    private final String issuer;
    private final RsaKeyProps rsaKeyProps;
    private final JwtEncoder jwtEncoder;

    public JwtService(
            @Value("${spring.application.name}") String issuer,
            RsaKeyProps rsaKeyProps,
            JwtEncoder jwtEncoder
    ) {
        this.issuer = issuer;
        this.rsaKeyProps = rsaKeyProps;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponse generateToken(User user) {
        var expiresAt = Instant.now().plus(rsaKeyProps.getTtl());
        var claimsSet = JwtClaimsSet.builder()
                                    .subject(user.getEmail())
                                    .id(user.getId().toString())
                                    .issuer(issuer)
                                    .expiresAt(expiresAt)
                                    .build();

        return new LoginResponse(
                jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue(),
                LocalDateTime.ofInstant(expiresAt, ZoneId.systemDefault())
        );
    }
}
