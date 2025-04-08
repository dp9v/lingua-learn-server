package com.cp9v.lingua_learn_server.api.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rsa")
@Getter
@Setter
public class RsaKeyProps {
    private String publicKey;
    private String privateKey;
    private long lifetimeHours;
}
