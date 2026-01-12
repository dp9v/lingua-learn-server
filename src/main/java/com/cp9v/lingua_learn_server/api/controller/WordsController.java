package com.cp9v.lingua_learn_server.api.controller;

import com.cp9v.lingua_learn_server.api.dto.requests.GetRandomWordsRequest;
import com.cp9v.lingua_learn_server.api.dto.responses.GetRandomWordsResponse;
import com.cp9v.lingua_learn_server.api.service.GroupsService;
import com.cp9v.lingua_learn_server.api.service.WordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/words")
public class WordsController {
    private final WordsService wordsService;
    private final GroupsService groupsService;

    @GetMapping("/random")
    public ResponseEntity<GetRandomWordsResponse> getRandomWords(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute GetRandomWordsRequest request
    ) {
        var userId = Long.parseLong(jwt.getId());
        
        // Validate that all requested groups belong to the authenticated user
        for (Long groupId : request.groups()) {
            groupsService.getGroup(groupId, userId);
        }
        
        return ResponseEntity.ok(
                new GetRandomWordsResponse(
                        wordsService.getRandomPairs(request.groups(), request.count()))
        );
    }
}
