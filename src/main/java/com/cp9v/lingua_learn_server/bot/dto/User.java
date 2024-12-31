package com.cp9v.lingua_learn_server.bot.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class User {
    public final int id;
    public final boolean isBot;
    public final String firstName;
    public final String lastName;
    public final String username;
}
