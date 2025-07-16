package com.cp9v.lingua_learn_server.api.dto;

import com.cp9v.lingua_learn_server.api.dto.enums.Languages;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(exclude = "id")
public final class Word {
    private final long id;
    private final String text;
    private final Languages language;

    public Word(ResultSet rs, int i) throws SQLException {
        this(
                rs.getLong("id"),
                rs.getString("text"),
                rs.getObject("language", Languages.class)
        );
    }
}
