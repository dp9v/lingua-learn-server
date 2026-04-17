package com.dp9v.libretranslate.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "libretranslate")
public class LibreTranslateProperties {

    /**
     * Base URL of the self-hosted LibreTranslate instance.
     */
    private String url;
}

