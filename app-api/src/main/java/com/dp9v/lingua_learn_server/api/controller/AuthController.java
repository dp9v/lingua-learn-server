package com.dp9v.lingua_learn_server.api.controller;

import com.dp9v.lingua_learn_server.api.components.JwtService;
import com.dp9v.lingua_learn_server.api.dto.UserDto;
import com.dp9v.lingua_learn_server.api.dto.auth.LoginResponse;
import com.dp9v.lingua_learn_server.api.dto.auth.MeResponse;
import com.dp9v.lingua_learn_server.api.dto.auth.RegisterRequest;
import com.dp9v.lingua_learn_server.api.models.User;
import com.dp9v.lingua_learn_server.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest userDto) {
        userService.create(userDto);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserDto userDto) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password())
        );
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(
                jwtService.generateToken(user)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> getUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(
                new MeResponse(
                        auth.getName()
                )
        );
    }
}
