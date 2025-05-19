package com.cp9v.lingua_learn_server.api.dto;

public record WordPair(
        long id,
        Word from,
        Word to
) {
}
