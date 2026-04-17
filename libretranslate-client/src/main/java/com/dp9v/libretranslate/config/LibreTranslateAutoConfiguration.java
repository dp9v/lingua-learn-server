package com.dp9v.libretranslate.config;

import com.dp9v.libretranslate.LibreTranslateClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(LibreTranslateProperties.class)
public class LibreTranslateAutoConfiguration {

    @Bean
    public RestClient libreTranslateRestClient(LibreTranslateProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.getUrl())
                .build();
    }

    @Bean
    public LibreTranslateClient libreTranslateClient(RestClient libreTranslateRestClient) {
        return new LibreTranslateClient(libreTranslateRestClient);
    }
}


