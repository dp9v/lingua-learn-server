package com.cp9v.lingua_learn_server.api.controller;

import com.cp9v.lingua_learn_server.api.dto.requests.GetRandomWordsRequest;
import com.cp9v.lingua_learn_server.api.dto.responses.GetRandomWordsResponse;
import com.cp9v.lingua_learn_server.api.service.WordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/words")
public class WordsController {
    private final WordsService wordsService;

    @GetMapping("/random")
    public ResponseEntity<GetRandomWordsResponse> getRandomWords(
            @ModelAttribute GetRandomWordsRequest request
    ) {
        return ResponseEntity.ok(
                new GetRandomWordsResponse(
                        wordsService.getRandomPairs(request.groups(), request.count()))
        );
    }
}
