package com.cp9v.lingua_learn_server.api.dto;

import com.cp9v.lingua_learn_server.api.dto.enums.Languages;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Word(
        long id,
        String text,
        Languages language
) {
    public Word(ResultSet rs, int i) throws SQLException {
        this(
                rs.getLong("id"),
                rs.getString("text"),
                rs.getObject("language", Languages.class)
        );
    }
}
