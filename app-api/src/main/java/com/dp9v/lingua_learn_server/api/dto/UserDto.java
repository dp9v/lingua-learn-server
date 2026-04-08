package com.dp9v.lingua_learn_server.api.dto;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String email,
        String password,
        String name,
        String surname
) {
}
