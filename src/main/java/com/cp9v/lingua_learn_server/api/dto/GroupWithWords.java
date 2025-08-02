package com.cp9v.lingua_learn_server.api.dto;

import java.util.List;

public record GroupWithWords(
        Group group,
        List<WordPairFull> words
) {}
