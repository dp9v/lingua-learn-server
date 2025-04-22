package com.cp9v.lingua_learn_server.api.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

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

    public String generateToken(final String username) {
        final var claimsSet = JwtClaimsSet.builder()
                .subject(username)
                .issuer(issuer)
                .expiresAt(Instant.now().plus(rsaKeyProps.getTtl()))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
