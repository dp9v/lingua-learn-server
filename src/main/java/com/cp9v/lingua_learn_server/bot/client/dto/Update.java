package com.cp9v.lingua_learn_server.bot.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Update {
    private static final String UPDATE_ID_FIELD = "update_id";
    private static final String MESSAGE_FIELD = "message";

    @JsonProperty(UPDATE_ID_FIELD)
    public final int id;

    @JsonProperty(MESSAGE_FIELD)
    public final Message message;
}
