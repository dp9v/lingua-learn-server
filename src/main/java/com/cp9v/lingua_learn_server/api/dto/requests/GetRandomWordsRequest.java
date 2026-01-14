package com.cp9v.lingua_learn_server.api.dto.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record GetRandomWordsRequest(
        @NotEmpty
        @Size(max = 25)
        List<Long> groups,

        @Max(30)
        int count) {}
