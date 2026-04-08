package com.dp9v.lingua_learn_server.bot.pipelines;

import com.dp9v.lingua_learn_server.bot.dto.FlatUpdate;
import com.dp9v.lingua_learn_server.bot.dto.PipelineStepInfo;

public interface PipelineStep {
    String getFullStepName();

    PipelineStepInfo process(PipelineStepInfo stepInfo, FlatUpdate update);
}
