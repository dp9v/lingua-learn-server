package com.cp9v.lingua_learn_server.bot.client.repositories;

import com.cp9v.lingua_learn_server.bot.client.dto.FastUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
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

    public void create(List<FastUpdate> updates) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO bot__fast_updates (update_id, date, json) VALUES (?, ?, ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        var update = updates.get(i);
                        ps.setInt(1, update.updateId);
                        ps.setTimestamp(2, new Timestamp(update.date * 1000));
                        ps.setObject(3, update.json, Types.OTHER);
                    }

                    @Override
                    public int getBatchSize() {
                        return updates.size();
                    }
                });
    }
}
