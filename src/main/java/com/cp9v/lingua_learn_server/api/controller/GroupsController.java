package com.cp9v.lingua_learn_server.api.controller;

import com.cp9v.lingua_learn_server.api.dto.Group;
import com.cp9v.lingua_learn_server.api.dto.WordPair;
import com.cp9v.lingua_learn_server.api.service.GroupsService;
import com.cp9v.lingua_learn_server.api.service.WordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupsController {

    private final GroupsService groupsService;
    private final WordsService wordsService;

    @GetMapping
    public ResponseEntity<List<Group>> getGroupsForUser(@AuthenticationPrincipal Jwt jwt) {
        var userId = Long.parseLong(jwt.getId());
        var groups = groupsService.getGroupsForUser(userId);
        return ResponseEntity.ok(groups);
    }

    @GetMapping
    @RequestMapping("/{groupId}/words")
    public ResponseEntity<List<WordPair>> getWordsInGroup(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable long groupId
    ) {
        var userId = Long.parseLong(jwt.getId());
        groupsService.checkAvailability(groupId, userId);
        var result = wordsService.getPairsForGroup(groupId);
        return ResponseEntity.ok(result);
    }
}
