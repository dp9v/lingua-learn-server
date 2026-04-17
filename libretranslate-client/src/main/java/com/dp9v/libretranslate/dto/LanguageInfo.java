package com.dp9v.libretranslate.dto;

import java.util.List;

public record LanguageInfo(
        LanguageCode code,
        String name,
        List<LanguageCode> targets
) {
}

