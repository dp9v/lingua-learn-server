package com.cp9v.lingua_learn_server.bot.pipelines;

import com.cp9v.lingua_learn_server.bot.dto.FlatUpdate;
import com.cp9v.lingua_learn_server.bot.dto.PipelineStepInfo;

public interface PipelineStep {
    String getFullStepName();

    PipelineStepInfo process(PipelineStepInfo stepInfo, FlatUpdate update);
}
