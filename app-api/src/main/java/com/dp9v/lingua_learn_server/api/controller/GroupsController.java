package com.dp9v.lingua_learn_server.api.controller;

import com.dp9v.lingua_learn_server.api.dto.Group;
import com.dp9v.lingua_learn_server.api.dto.GroupWithWords;
import com.dp9v.lingua_learn_server.api.dto.requests.CreateWordsRequest;
import com.dp9v.lingua_learn_server.api.service.GroupsService;
import com.dp9v.lingua_learn_server.api.service.UserService;
import com.dp9v.lingua_learn_server.api.service.WordsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupsController {

    private final GroupsService groupsService;
    private final WordsService wordsService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Group>> getGroupsForUser(@AuthenticationPrincipal Jwt jwt) {
        var userId = Long.parseLong(jwt.getId());
        var groups = groupsService.getGroupsForUser(userId);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{groupId}/words")
    public ResponseEntity<GroupWithWords> getWordsInGroup(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable long groupId
    ) {
        var userId = Long.parseLong(jwt.getId());
        var group = groupsService.getGroup(groupId, userId);
        var wordPairs = wordsService.getPairsForGroup(groupId);
        return ResponseEntity.ok(new GroupWithWords(group, wordPairs));
    }

    @PostMapping
    public ResponseEntity<Long> createGroup(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody Group group) {
        var groupId = groupsService.createGroup(group);
        userService.addGroup(
                Long.parseLong(jwt.getId()),
                groupId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(groupId);
    }

    @PutMapping("/{groupId}/words")
    public ResponseEntity<String> addWords(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable long groupId,
            @Valid @RequestBody CreateWordsRequest createWordsRequest
    ) {
        groupsService.addPairs(groupId, Long.parseLong(jwt.getId()), createWordsRequest.pairs());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .build();
    }
}
