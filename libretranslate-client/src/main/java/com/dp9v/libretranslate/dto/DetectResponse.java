package com.dp9v.libretranslate.dto;

public record DetectResponse(
        double confidence,
        LanguageCode language
) {
}

