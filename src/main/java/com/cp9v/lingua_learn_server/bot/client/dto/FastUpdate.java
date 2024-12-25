package com.cp9v.lingua_learn_server.bot.client.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class FastUpdate {
    public final int updateId;
    public final String json;
    public final long date;
}
