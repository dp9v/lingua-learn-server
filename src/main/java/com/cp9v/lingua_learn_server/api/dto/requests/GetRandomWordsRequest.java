package com.cp9v.lingua_learn_server.api.dto.requests;

import java.util.List;

public record GetRandomWordsRequest(
        List<Long> groups,
        int count) {}
