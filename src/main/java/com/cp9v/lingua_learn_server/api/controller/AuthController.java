package com.cp9v.lingua_learn_server.api.controller;

import com.cp9v.lingua_learn_server.api.components.JwtService;
import com.cp9v.lingua_learn_server.api.dto.UserDto;
import com.cp9v.lingua_learn_server.api.models.User;
import com.cp9v.lingua_learn_server.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public void register(@RequestBody UserDto userDto) {
        userService.create(userDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDto userDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password())
        );
        var user = User.builder()
                       .email(userDto.email())
                       .password(userDto.password())
                       .build();
        return jwtService.generateJwt(user);
    }
}
