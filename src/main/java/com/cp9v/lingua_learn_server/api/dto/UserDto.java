package com.cp9v.lingua_learn_server.api.dto;

public record UserDto(
        Long id,
        String email,
        String password,
        String name,
        String surname
) {
}
