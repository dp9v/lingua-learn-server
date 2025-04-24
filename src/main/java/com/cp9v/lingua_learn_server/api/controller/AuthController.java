package com.cp9v.lingua_learn_server.api.controller;

import com.cp9v.lingua_learn_server.api.components.JwtService;
import com.cp9v.lingua_learn_server.api.dto.LoginResponse;
import com.cp9v.lingua_learn_server.api.dto.MeResponse;
import com.cp9v.lingua_learn_server.api.dto.UserDto;
import com.cp9v.lingua_learn_server.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody UserDto userDto) {
        userService.create(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserDto userDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password())
        );
        return ResponseEntity.ok(
                new LoginResponse(
                        jwtService.generateToken(userDto.email())
                )
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
