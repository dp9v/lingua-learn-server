package com.dp9v.lingua_learn_server.api.dto.auth;

import java.time.LocalDateTime;

public record LoginResponse(
        String token,
        LocalDateTime expiresAt
) {
}
