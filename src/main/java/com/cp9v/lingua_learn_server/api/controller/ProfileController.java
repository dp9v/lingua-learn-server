package com.cp9v.lingua_learn_server.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ProfileController {

    @GetMapping("/me")
    public String getUserName(Authentication authentication) {
        return authentication.getName();
    }
}
