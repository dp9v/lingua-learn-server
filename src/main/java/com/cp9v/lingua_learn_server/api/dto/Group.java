package com.cp9v.lingua_learn_server.api.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Group(
        Long id,
        String name,
        String description,
        boolean isPublic,
        long wordsCount
) {
    public Group(ResultSet rs, int i) throws SQLException {
        this(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("is_public"),
                rs.getInt("words_count")
        );
    }
}
