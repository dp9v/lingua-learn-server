package com.dp9v.lingua_learn_server.api.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public record WordPair(
        Long id,
        long fromId,
        long toId
) {
    public WordPair(ResultSet rs, int i) throws SQLException {
        this(
                rs.getLong("id"),
                rs.getLong("fromId"),
                rs.getLong("toId")
        );
    }
}
