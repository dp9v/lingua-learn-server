package com.cp9v.lingua_learn_server.bot.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public final class User {
    private static final String ID_FIELD = "id";
    private static final String FIRST_NAME_FIELD = "first_name";
    private static final String IS_BOT_FIELD = "is_bot";
    private static final String LAST_NAME_FIELD = "last_name";

    @JsonProperty(ID_FIELD)
    public final int id;

    @JsonProperty(IS_BOT_FIELD)
    public final boolean isBot;

    @JsonProperty(FIRST_NAME_FIELD)
    public final String firstName;

    @JsonProperty(LAST_NAME_FIELD)
    public final String lastName;
}
