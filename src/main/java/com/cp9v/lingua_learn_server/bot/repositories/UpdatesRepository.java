package com.cp9v.lingua_learn_server.bot.repositories;

import com.cp9v.lingua_learn_server.bot.dto.FlatUpdate;
import com.cp9v.lingua_learn_server.bot.dto.Update;
import com.cp9v.lingua_learn_server.bot.dto.enums.UpdateStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UpdatesRepository {
    private final JdbcTemplate jdbcTemplate;

    public void save(List<Update> updates) {
        var updatesData = updates.stream()
                .map(u -> new Object[]{
                        u.id,
                        u.message.id,
                        u.message.from.id,
                        new Timestamp(u.message.date * 1000L),
                        u.message.text
                })
                .toList();

        jdbcTemplate.batchUpdate(
                "INSERT INTO bot__updates (update_id, message_id, userid, date, text) VALUES (?, ?, ?, ?, ?)",
                updatesData);
    }
}
