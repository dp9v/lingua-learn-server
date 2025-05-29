package com.cp9v.lingua_learn_server.api.dto;

import com.cp9v.lingua_learn_server.api.dto.enums.Languages;

import java.sql.ResultSet;
import java.sql.SQLException;

public record WordPair(
        long id,
        Word from,
        Word to
) {
    public WordPair(ResultSet rs, int i) throws SQLException {
        this(rs.getLong("id"),
             new Word(rs.getLong("from_word_id"),
                      rs.getString("from_word_text"),
                      Languages.valueOf(rs.getString("from_word_lang"))),
             new Word(rs.getLong("to_word_id"),
                      rs.getString("to_word_text"),
                      Languages.valueOf(rs.getString("to_word_lang")))
        );
    }
}
