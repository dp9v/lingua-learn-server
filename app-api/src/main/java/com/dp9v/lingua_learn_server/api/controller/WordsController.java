package com.dp9v.lingua_learn_server.api.controller;

import com.dp9v.lingua_learn_server.api.dto.requests.GetRandomWordsRequest;
import com.dp9v.lingua_learn_server.api.dto.responses.GetRandomWordsResponse;
import com.dp9v.lingua_learn_server.api.service.GroupsService;
import com.dp9v.lingua_learn_server.api.service.WordsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/words")
public class WordsController {
    private final WordsService wordsService;
    private final GroupsService groupsService;

    @GetMapping("/random")
    public ResponseEntity<GetRandomWordsResponse> getRandomWords(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute @Valid GetRandomWordsRequest request
    ) {
        groupsService.validateAccess(new HashSet<>(request.groups()), Long.parseLong(jwt.getId()));
        return ResponseEntity.ok(
                new GetRandomWordsResponse(
                        wordsService.getRandomPairs(request.groups(), request.count()))
        );
    }
}
