package com.cp9v.lingua_learn_server.api.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
@RequiredArgsConstructor
public class GroupPairsRepository {
    private final JdbcTemplate jdbcTemplate;

    public int[] addPairs(int groupId, int[] pairIds) {
        var query = "INSERT INTO public.api__group_word_pairs (group_id, word_pair_id) VALUES (?, ?)";
        var updates = Arrays.stream(pairIds)
                            .boxed()
                            .map(id -> new Object[]{groupId, id})
                            .toList();

        return jdbcTemplate.batchUpdate(query, updates);
    }
}
