package com.cp9v.lingua_learn_server.api.repositories;

import com.cp9v.lingua_learn_server.api.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Slf4j
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public Optional<User> findByEmail(String email) {
        var result = jdbcTemplate.query("SELECT * FROM api__users WHERE email=?", User::new, email);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        if (result.size() > 1) {
            throw new IllegalStateException("More than one user found for email: " + email);
        }
        return Optional.of(result.getFirst());
    }

    public void save(User user) {
        var res = jdbcTemplate.update("INSERT INTO api__users (email, password, name, surname) VALUES (?, ?, ?, ?);",
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getSurname());
        log.info("Inserted user with id: {}", res);
    }

    public void addGroup(long userId, long groupId) {
        jdbcTemplate.update("INSERT INTO api__user_groups (user_id, group_id) VALUES (?, ?);", userId, groupId);
    }

}
