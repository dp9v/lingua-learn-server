package com.cp9v.lingua_learn_server.api.repositories;

import com.cp9v.lingua_learn_server.api.dto.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class WordsRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<Word> words) {
        String sql = "INSERT INTO api__words (text, language) VALUES (?, ?)";
        var updates = words.stream()
                           .map(u -> new Object[]{
                                   u.text(),
                                   u.language().name(),
                           }).toList();

        jdbcTemplate.batchUpdate(sql, updates);
    }

    public void saveAllNewWords(Set<Word> words) {
        String sql = """
                    INSERT INTO api__words (text, language)
                    VALUES (?, ?)
                    ON CONFLICT (text, language) DO NOTHING
                """;

        var updates = words.stream()
                           .map(word -> new Object[]{
                                   word.text(),
                                   word.language().name()
                           })
                           .toList();
        jdbcTemplate.batchUpdate(sql, updates);
    }
}
