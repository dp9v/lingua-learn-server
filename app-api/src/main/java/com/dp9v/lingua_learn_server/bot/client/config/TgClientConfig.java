package com.dp9v.lingua_learn_server.bot.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class TgClientConfig {
    private static final String TG_BOT_API_URL = "https://api.telegram.org/bot%s/";

    @Bean
    public RestTemplate tgRestTemplate(
            @Value("${tg.bot.timeout:10}") int timeout,
            @Value("${tg.bot.token:}") String token,
            RestTemplateBuilder restTemplateBuilder
    ) {
        return restTemplateBuilder
                .rootUri(TG_BOT_API_URL.formatted(token))
                .setReadTimeout(Duration.of(timeout, ChronoUnit.SECONDS))
                .setConnectTimeout(Duration.of(timeout, ChronoUnit.SECONDS))
                .build();
    }
}
