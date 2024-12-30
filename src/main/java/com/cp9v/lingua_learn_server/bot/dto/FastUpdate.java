package com.cp9v.lingua_learn_server.bot.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@RequiredArgsConstructor
@Builder
public class FastUpdate {
    public final int updateId;
    public final String json;
    public final long date;

    public FastUpdate(ResultSet rs, int ignoredRowNum) throws SQLException {
        updateId = rs.getInt("update_id");
        json = rs.getString("json");
        date = Timestamp.valueOf(rs.getString("date")).getTime();
    }
}
