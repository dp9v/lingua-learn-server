package com.cp9v.lingua_learn_server.bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chat {
    private static final String ID_FIELD = "id";
    private static final String TYPE_FIELD = "type";

    @JsonProperty(ID_FIELD)
    public final int id;

    @JsonProperty(TYPE_FIELD)
    public final String type;
}
