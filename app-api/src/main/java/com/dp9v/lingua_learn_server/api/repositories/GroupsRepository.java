package com.dp9v.lingua_learn_server.api.repositories;

import com.dp9v.lingua_learn_server.api.dto.Group;
import com.dp9v.lingua_learn_server.api.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupsRepository {
    private final JdbcTemplate jdbcTemplate;

    public long createGroup(Group group) {
        String sql = "INSERT INTO api__groups (name, description) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, group.name());
            ps.setString(2, group.description());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<Group> getAllForUser(long userId) {
        String sql = """
                SELECT
                    api__groups.id,
                    name,
                    description,
                    is_public,
                    COUNT(DISTINCT api__words.id) as words_count
                FROM api__groups
                INNER JOIN api__user_groups ON api__groups.id = api__user_groups.group_id
                LEFT JOIN api__group_word_pairs ON api__groups.id = api__group_word_pairs.group_id
                LEFT JOIN api__words ON api__words.id = api__group_word_pairs.word_pair_id
                WHERE api__user_groups.user_id = ?
                GROUP BY api__groups.id, name, description, is_public
                """;
        return jdbcTemplate.query(sql, Group::new, userId);
    }

    public Optional<Group> getByIdAndUserId(long groupId, long userId) {
        String sql = """
                SELECT
                    api__groups.id,
                    name,
                    description,
                    is_public,
                    0 as words_count
                FROM api__groups
                LEFT JOIN api__user_groups ON api__groups.id = api__user_groups.group_id
                WHERE is_public=true AND api__groups.id=?
                   OR api__user_groups.user_id=? AND api__groups.id=?
                """;
        List<Group> result = jdbcTemplate.query(sql, Group::new, groupId, userId, groupId);
        return Utils.toOptional(result);
    }

    public void addNewPairs(long groupId, List<Long> pairs) {
        var sql = "INSERT INTO api__group_word_pairs (group_id, word_pair_id) " +
                  "VALUES (?, ?)" +
                  "ON CONFLICT (group_id, word_pair_id) DO NOTHING";
        var updates = pairs.stream().map(p -> new Object[]{groupId, p}).toList();
        jdbcTemplate.batchUpdate(sql, updates);
    }
}
