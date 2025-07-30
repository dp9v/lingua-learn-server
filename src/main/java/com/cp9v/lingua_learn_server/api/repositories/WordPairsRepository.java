package com.cp9v.lingua_learn_server.api.repositories;

import com.cp9v.lingua_learn_server.api.dto.WordPair;
import com.cp9v.lingua_learn_server.api.dto.WordPairFull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class WordPairsRepository {

    private final JdbcTemplate jdbcTemplate;

    public void createNewPairs(List<long[]> pairs) {
        var query = """
                INSERT INTO api__word_pairs (word_from_id, word_to_id)
                VALUES (?, ?)
                ON CONFLICT (word_from_id, word_to_id) DO NOTHING
                """;
        var updates = pairs.stream().map(p -> new Object[]{
                p[0],
                p[1]
        }).toList();

        jdbcTemplate.batchUpdate(query, updates);
    }

    public List<WordPair> findAllPairs(List<long[]> pairs) {
        var inClause = pairs.stream()
                            .map(w -> "(?, ?)")
                            .collect(Collectors.joining(", "));
        var query = """
                    SELECT id, word_from_id as fromId, word_to_id as toId
                    FROM api__word_pairs
                    WHERE (word_from_id, word_to_id) IN (%s)
                """.formatted(inClause);
        Object[] args = pairs.stream()
                             .flatMap(p -> Stream.of(p[0], p[1]))
                             .toArray();
        return jdbcTemplate.query(query, WordPair::new, args);
    }

    public List<WordPairFull> getPairsForGroup(long groupId) {
        var query = """
                SELECT
                    api__word_pairs.id,
                    from_word.id as from_word_id,
                    from_word.text as from_word_text,
                    from_word.language as from_word_lang,
                    to_word.id as to_word_id,
                    to_word.text as to_word_text,
                    to_word.language as to_word_lang
                FROM api__word_pairs
                INNER JOIN api__group_word_pairs ON api__word_pairs.id = api__group_word_pairs.word_pair_id
                INNER JOIN api__words from_word ON api__word_pairs.word_from_id = from_word.id
                INNER JOIN api__words to_word ON api__word_pairs.word_to_id = to_word.id
                WHERE api__group_word_pairs.group_id = ?
                """;
        return jdbcTemplate.query(query, WordPairFull::new, groupId);
    }
}
