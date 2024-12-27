package com.cp9v.lingua_learn_server.bot.client.repositories;

import com.cp9v.lingua_learn_server.bot.client.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAllIfNotExist(List<User> users) {
        List<Object[]> userData = users.stream()
                .map(u -> new Object[]{u.id, u.isBot, u.firstName, u.lastName, u.username}
                ).toList();
        jdbcTemplate.batchUpdate("""
                INSERT INTO bot__users (id, is_bot, first_name, last_name, username) 
                VALUES (?, ?, ?, ?, ?) 
                ON CONFLICT (id) DO NOTHING""", userData);
    }
}
