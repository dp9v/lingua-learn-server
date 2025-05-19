package com.cp9v.lingua_learn_server.api.repositories;

import com.cp9v.lingua_learn_server.api.dto.WordPair;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WordPairsRepository {

    private final JdbcTemplate jdbcTemplate;

    public int[] createPairs(List<WordPair> pairs) {
        var query = "INSERT INTO api__word_pairs (word_from_id, word_to_id) VALUES (?, ?)";
        var updates = pairs.stream().map(p -> new Object[]{
                p.from().id(),
                p.to().id()
        }).toList();

        return jdbcTemplate.batchUpdate(query, updates);
    }

}
