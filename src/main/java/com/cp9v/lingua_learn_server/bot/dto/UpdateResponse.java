package com.cp9v.lingua_learn_server.bot.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Builder
public class UpdateResponse {
    public final boolean ok;
    public final List<Update> result;
}
