package com.dp9v.lingua_learn_server.bot.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class ResponseMessage {
    private final static String CHAT_ID_FIELD = "chat_id";
    private final static String TEXT_FIELD = "text";

    @JsonProperty(CHAT_ID_FIELD)
    private final long chat_id;

    @JsonProperty(TEXT_FIELD)
    private final String text;
}
