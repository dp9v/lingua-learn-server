package com.cp9v.lingua_learn_server.api.repositories;

import com.cp9v.lingua_learn_server.api.models.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WordsRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Word> getAll() {
        return jdbcTemplate.query("SELECT * FROM words", Word::new);
    }

}
