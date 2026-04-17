package com.dp9v.libretranslate;

import com.dp9v.libretranslate.dto.DetectResponse;
import com.dp9v.libretranslate.dto.LanguageCode;
import com.dp9v.libretranslate.dto.LanguageInfo;
import com.dp9v.libretranslate.dto.TranslateRequest;
import com.dp9v.libretranslate.dto.TranslateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class LibreTranslateClient {

    private final RestClient libreTranslateRestClient;

    /**
     * GET /languages — list of supported languages.
     */
    public List<LanguageInfo> getLanguages() {
        return libreTranslateRestClient.get()
                .uri("/languages")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    /**
     * POST /translate — translate text.
     */
    public TranslateResponse translate(String text, LanguageCode source, LanguageCode target) {
        return libreTranslateRestClient.post()
                .uri("/translate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TranslateRequest(text, source, target))
                .retrieve()
                .body(TranslateResponse.class);
    }

    /**
     * POST /detect — detect language of a text.
     */
    public List<DetectResponse> detect(String text) {
        return libreTranslateRestClient.post()
                .uri("/detect")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("q", text))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}




