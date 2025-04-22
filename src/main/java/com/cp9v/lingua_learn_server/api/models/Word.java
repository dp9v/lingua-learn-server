package com.cp9v.lingua_learn_server.api.models;

import lombok.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
public record Word(
        long id,
        String word,
        String meaning
) {

    public Word(ResultSet rs, int ignoredRowNum) throws SQLException {
        this(
                rs.getLong("id"),
                rs.getString("word"),
                rs.getString("meaning")
        );
    }
}
