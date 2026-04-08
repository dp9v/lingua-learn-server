package com.dp9v.lingua_learn_server.bot.repositories;

import com.dp9v.lingua_learn_server.bot.dto.PipelineStepInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PipelineStepsRepository {

    private final JdbcTemplate jdbcTemplate;

    public Optional<PipelineStepInfo> getLastStepForUser(int userId) {
        var query = """
                SELECT
                    bot__user_statuses.id,
                    bot__user_statuses.update_id,
                    bot__user_statuses.user_id,
                    bot__user_statuses.pipeline_name,
                    bot__user_statuses.pipeline_step_name,
                    bot__user_statuses.payload,
                    bot__user_statuses.created_time
                FROM bot__user_statuses
                WHERE user_id = ?
                ORDER BY created_time DESC
                LIMIT 1;
                """;
        return jdbcTemplate.query(query, PipelineStepInfo::new, userId)
                .stream()
                .findFirst();
    }

    public void insert(PipelineStepInfo pipelineStepInfo) {
        var query = """
                INSERT INTO bot__user_statuses (update_id, user_id, pipeline_name, pipeline_step_name, payload) 
                VALUES (?, ?, ?, ?, ?::json);
                """;
        jdbcTemplate.update(query,
                pipelineStepInfo.updateId,
                pipelineStepInfo.userId,
                pipelineStepInfo.pipelineName,
                pipelineStepInfo.pipelineStep,
                pipelineStepInfo.payload);
    }
}