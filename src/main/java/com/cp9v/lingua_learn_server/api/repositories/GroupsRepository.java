package com.cp9v.lingua_learn_server.api.repositories;

import com.cp9v.lingua_learn_server.api.models.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@RequiredArgsConstructor
public class GroupsRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Word> getGroupsForUser(int userId) {
        jdbcTemplate.
    }
}
