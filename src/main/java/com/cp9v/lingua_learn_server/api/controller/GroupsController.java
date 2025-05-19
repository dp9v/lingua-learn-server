package com.cp9v.lingua_learn_server.api.controller;

import com.cp9v.lingua_learn_server.api.models.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupsController {

    @GetMapping
    public void getGroupsForUser() {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
    }

    @GetMapping
    @RequestMapping("/{groupId}/words")
    public void getWordsInGroup(Authentication auth, @PathVariable long groupId) {

    }
}
