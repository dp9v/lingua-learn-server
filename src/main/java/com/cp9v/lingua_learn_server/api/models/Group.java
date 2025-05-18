package com.cp9v.lingua_learn_server.api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Builder
@RequiredArgsConstructor
public class Group {
    private final long id;
    private final String name;
    private final String description;
    private final int wordsCount;

    public Group(ResultSet rs, int i) throws SQLException {
        this(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("wordsCount")
        );
    }
}
