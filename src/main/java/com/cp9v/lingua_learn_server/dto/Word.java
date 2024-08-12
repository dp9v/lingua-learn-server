package com.cp9v.lingua_learn_server.dto;

import lombok.Builder;

@Builder
public record Word(
    long id,
    String word,
    String meaning
) {
}
