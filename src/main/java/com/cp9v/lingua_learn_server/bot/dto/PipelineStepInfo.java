package com.cp9v.lingua_learn_server.bot.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Builder
public class PipelineStepInfo {
    public final long id;
    public final int userId;
    public final int updateId;
    public final String pipelineName;
    public final String pipelineStep;
    public final LocalDateTime createTime;
    public final String payload;

    public PipelineStepInfo(ResultSet rs, int ignoredRowNum) throws SQLException {
        this.id = rs.getLong("id");
        this.userId = rs.getInt("user_id");
        this.updateId = rs.getInt("update_id");
        this.pipelineName = rs.getString("pipeline_name");
        this.pipelineStep = rs.getString("pipeline_step_name");
        this.createTime = rs.getTimestamp("created_time").toLocalDateTime();
        this.payload = rs.getString("payload");
    }

    public String getFullStepName() {
        return "%s::%s".formatted(pipelineName, pipelineStep);
    }
}
