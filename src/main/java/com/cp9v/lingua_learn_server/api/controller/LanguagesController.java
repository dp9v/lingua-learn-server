package com.cp9v.lingua_learn_server.api.controller;

import com.cp9v.lingua_learn_server.api.dto.LanguageDto;
import com.cp9v.lingua_learn_server.api.dto.enums.Languages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguagesController {

    @GetMapping
    public ResponseEntity<List<LanguageDto>> getAvailableLanguages() {
        return ResponseEntity.ok(
                Arrays.stream(Languages.values())
                      .map(l -> new LanguageDto(l.name(), l.fullName))
                      .toList());
    }
}
