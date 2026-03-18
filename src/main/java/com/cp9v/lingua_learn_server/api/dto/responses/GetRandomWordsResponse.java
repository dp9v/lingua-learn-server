package com.cp9v.lingua_learn_server.api.dto.responses;

import com.cp9v.lingua_learn_server.api.dto.WordPairFull;

import java.util.List;

public record GetRandomWordsResponse(
        List<WordPairFull> words
) {}
