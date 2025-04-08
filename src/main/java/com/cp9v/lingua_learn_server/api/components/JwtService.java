package com.cp9v.lingua_learn_server.api.components;

import com.cp9v.lingua_learn_server.api.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final RsaKeyProps rsaKeyProps;

    public String generateJwt(User user) {

    }
}
