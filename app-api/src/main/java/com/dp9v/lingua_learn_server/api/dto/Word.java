package com.dp9v.lingua_learn_server.api.dto;

import com.dp9v.lingua_learn_server.api.dto.enums.Languages;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Getter
@EqualsAndHashCode(exclude = "id")
@ToString
public final class Word {
    private final Long id;
    private final String text;
    private final Languages language;

    public Word(ResultSet rs, int i) throws SQLException {
        this(
                rs.getLong("id"),
                rs.getString("text"),
                Languages.valueOf(rs.getString("language"))
        );
    }
}

