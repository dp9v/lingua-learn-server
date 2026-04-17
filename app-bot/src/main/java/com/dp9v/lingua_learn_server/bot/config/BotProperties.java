package com.dp9v.lingua_learn_server.bot.config;

import lombok.Getter;
import org.apache.http.util.Asserts;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "bot")
public class BotProperties {

    private final String name;
    private final String token;

    public BotProperties(String name, String token) {
        Asserts.notBlank(name, "Bot name");
        Asserts.notBlank(token, "Bot token");
        this.name = name;
        this.token = token;
    }
}

