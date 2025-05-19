package com.cp9v.lingua_learn_server.api.repositories;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.util.List;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.cp9v.lingua_learn_server.api.dto.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupsRepository {
    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<Group> groups) {
        var updates = groups.stream().map(u -> new Object[]{
                u.name(),
                u.description(),
        }).toList();

        String sql = "INSERT INTO api__groups (name, description) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sql, updates);
    }

    public List<Group> getAll() {
        String sql = """
                SELECT 
                    api__groups.id, 
                    name, 
                    description, 
                    is_public, 
                    COUNT(DISTINCT api__words.id) as words_count
                FROM api__groups
                INNER JOIN api__group_word_pairs ON api__groups.id = api__group_word_pairs.group_id
                INNER JOIN api__words ON api__words.id = api__group_word_pairs.word_pair_id
                GROUP BY api__groups.id, name, description, is_public
                """;
        return jdbcTemplate.query(sql, Group::new);
    }
}
