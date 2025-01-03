package com.cp9v.lingua_learn_server.bot.pipelines.calculate_nutrition_score;

import com.cp9v.lingua_learn_server.bot.client.TgClient;
import com.cp9v.lingua_learn_server.bot.client.dto.ResponseMessage;
import com.cp9v.lingua_learn_server.bot.dto.FlatUpdate;
import com.cp9v.lingua_learn_server.bot.dto.PipelineStepInfo;
import com.cp9v.lingua_learn_server.bot.pipelines.PipelineStep;
import com.cp9v.lingua_learn_server.bot.pipelines.Pipelines;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculateNutritionScoreStep implements PipelineStep {
    private final TgClient tgClient;

    @Override
    public String getFullStepName() {
        return "%s::%s".formatted(Pipelines.CALCULATE_NUTRITION_SCORE, Steps.CALCULATE_NUTRITION_SCORE);
    }

    @Override
    public PipelineStepInfo process(PipelineStepInfo stepInfo, FlatUpdate update) {
        tgClient.sendMessage(
                new ResponseMessage(update.chatId, "Tы все равно жирный, хочешь это сохранить?")
        );
        return PipelineStepInfo.builder()
                .userId(update.userId)
                .updateId(update.updateId)
                .payload(stepInfo.payload)
                .pipelineName(Pipelines.CALCULATE_NUTRITION_SCORE.name())
                .pipelineStep(Steps.SAVE_NUTRITION_SCORE.name())
                .build();
    }
}
