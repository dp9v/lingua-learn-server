package com.dp9v.libretranslate.dto;

public record TranslateRequest(
        String q,
        LanguageCode source,
        LanguageCode target
) {
}

