package com.dp9v.libretranslate.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LanguageCode {

    ENGLISH("en"),
    CZECH("cs"),
    RUSSIAN("ru"),
    AUTO_DETECT("auto");

    @JsonValue
    private final String code;

    @JsonCreator
    public static LanguageCode fromCode(String code) {
        for (LanguageCode lang : values()) {
            if (lang.code.equals(code)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Unknown language code: " + code);
    }
}

