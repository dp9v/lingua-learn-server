package com.cp9v.lingua_learn_server.bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    private static final String MESSAGE_ID_FIELD = "message_id";
    private static final String FROM_FIELD = "from";
    private static final String DATE_FIELD = "date";
    private static final String CHAT_FIELD = "chat";
    private static final String TEXT_FIELD = "text";

    @JsonProperty(MESSAGE_ID_FIELD)
    public final int id;

    @JsonProperty(FROM_FIELD)
    public final User from;

    @JsonProperty(DATE_FIELD)
    public final int date;

    @JsonProperty(TEXT_FIELD)
    public final String text;

    @JsonProperty(CHAT_FIELD)
    public final Chat chat;
}
