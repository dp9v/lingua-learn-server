package com.dp9v.lingua_learn_server.bot.mappers;

import com.dp9v.lingua_learn_server.bot.dto.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User map(com.dp9v.lingua_learn_server.bot.client.dto.User source) {
        return User.builder()
                .id(source.id)
                .isBot(source.isBot)
                .lastName(source.lastName)
                .firstName(source.firstName)
                .username(source.username)
                .build();
    }
}
