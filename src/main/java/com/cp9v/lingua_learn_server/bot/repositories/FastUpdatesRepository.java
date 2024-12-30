package com.cp9v.lingua_learn_server.bot.repositories;

import com.cp9v.lingua_learn_server.bot.dto.FastUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FastUpdatesRepository {

    private final JdbcTemplate jdbcTemplate;

    public int getLastUpdateId() {
        var updates = jdbcTemplate.queryForList(
                "SELECT update_id FROM bot__fast_updates ORDER BY date DESC LIMIT 1",
                Integer.class);
        if (updates.isEmpty()) {
            return 0;
        }
        return updates.getFirst();
    }

    public List<FastUpdate> getUnprocessedUpdates() {
        return jdbcTemplate.query(
                "SELECT bot__fast_updates.* " +
                        "FROM bot__fast_updates " +
                        "LEFT JOIN public.bot__updates bu on bot__fast_updates.update_id = bu.update_id " +
                        "WHERE bu.update_id IS NULL", FastUpdate::new);
    }

    public void save(List<FastUpdate> updates) {
        var updatesData = updates.stream()
                .map(u -> new Object[]{
                        u.updateId,
                        new Timestamp(u.date * 1000),
                        u.json
                })
                .toList();
        jdbcTemplate.batchUpdate(
                "INSERT INTO bot__fast_updates (update_id, date, json) VALUES (?, ?, ?);",
                updatesData
        );
    }
}
