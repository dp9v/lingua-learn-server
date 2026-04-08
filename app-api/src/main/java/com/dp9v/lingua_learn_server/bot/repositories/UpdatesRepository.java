package com.dp9v.lingua_learn_server.bot.repositories;

import com.dp9v.lingua_learn_server.bot.dto.FlatUpdate;
import com.dp9v.lingua_learn_server.bot.dto.enums.UpdateStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UpdatesRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void save(List<FlatUpdate> updates) {
        var updatesData = updates.stream().map(u -> new Object[]{
                u.updateId,
                u.messageId,
                u.chatId,
                u.userId,
                Timestamp.valueOf(u.date),
                u.text
        }).toList();

        jdbcTemplate.batchUpdate(
                "INSERT INTO bot__updates (update_id, message_id, chat_id, user_id, date, text) VALUES (?, ?, ?, ?, ?, ?)",
                updatesData
        );
    }

    public List<FlatUpdate> getAllNew() {
        var query = """
                SELECT bot__updates.update_id,
                       bot__updates.message_id,
                       bot__updates.chat_id,
                       bot__updates.user_id,
                       bot__users.username,
                       bot__updates.date,
                       bot__updates.text,
                       bot__updates.status
                FROM bot__updates
                INNER JOIN bot__users on bot__users.id = bot__updates.user_id
                WHERE bot__updates.status = ?""";
        return jdbcTemplate.query(query, FlatUpdate::new, UpdateStatus.NEW.toString());
    }

    public void markAsProcessed(List<Integer> updateIds) {
        if (updateIds == null || updateIds.isEmpty()) {
            return;
        }
        namedParameterJdbcTemplate.update(
                "UPDATE bot__updates SET status = :state WHERE update_id IN (:ids)",
                new MapSqlParameterSource(Map.of(
                        "state", UpdateStatus.PROCESSED.toString(),
                        "ids", updateIds)));
    }
}
