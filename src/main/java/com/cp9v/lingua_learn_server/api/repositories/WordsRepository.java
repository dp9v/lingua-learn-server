package com.cp9v.lingua_learn_server.api.repositories;

import com.cp9v.lingua_learn_server.api.dto.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class WordsRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAllNewWords(Set<Word> words) {
        String sql = """
                    INSERT INTO api__words (text, language)
                    VALUES (?, ?::language_code)
                    ON CONFLICT (text, language) DO NOTHING
                """;

        var updates = words.stream()
                           .map(word -> new Object[]{
                                   word.getText(),
                                   word.getLanguage().name()
                           })
                           .toList();
        jdbcTemplate.batchUpdate(sql, updates);
    }

    public List<Word> findAllByTextAndLanguage(Collection<Word> words) {
        String inClause = words.stream()
                               .map(w -> "(?, ?::language_code)")
                               .collect(Collectors.joining(", "));
        String sql = """
                    SELECT id, text, language
                    FROM api__words
                    WHERE (text, language) IN (%s)
                """.formatted(inClause);

        Object[] args = words.stream()
                             .flatMap(w -> Stream.of(w.getText(), w.getLanguage().name()))
                             .toArray();

        return jdbcTemplate.query(sql, Word::new, args);
    }

}
