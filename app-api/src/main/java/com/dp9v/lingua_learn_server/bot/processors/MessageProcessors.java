package com.dp9v.lingua_learn_server.bot.processors;

import com.dp9v.lingua_learn_server.bot.dto.PipelineStepInfo;
import com.dp9v.lingua_learn_server.bot.pipelines.PipelineStep;
import com.dp9v.lingua_learn_server.bot.pipelines.Pipelines;
import com.dp9v.lingua_learn_server.bot.pipelines.calculate_nutrition_score.Steps;
import com.dp9v.lingua_learn_server.bot.repositories.PipelineStepsRepository;
import com.dp9v.lingua_learn_server.bot.repositories.UpdatesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageProcessors {

    private final UpdatesRepository updatesRepository;
    private final PipelineStepsRepository pipelineStepsRepository;
    private final Map<String, PipelineStep> pipelineSteps;

    public MessageProcessors(
            UpdatesRepository updatesRepository,
            PipelineStepsRepository pipelineStepsRepository,
            List<PipelineStep> pipelineSteps
    ) {
        this.updatesRepository = updatesRepository;
        this.pipelineStepsRepository = pipelineStepsRepository;
        this.pipelineSteps = pipelineSteps
                .stream()
                .collect(Collectors.toMap(PipelineStep::getFullStepName, Function.identity()));
    }

    @Scheduled(fixedRate = 500)
    public void process() {
        var updatesByUsers = updatesRepository.getAllNew()
                .stream()
                .collect(Collectors.groupingBy(u -> u.userId));
        var processedUpdateIds = new ArrayList<Integer>();


        updatesByUsers.forEach((user, updates) -> {
            var firstUpdate = updates.stream()
                    .min(Comparator.comparing(u -> u.date))
                    .orElseThrow();

            var pipelineStepInfo = pipelineStepsRepository
                    .getLastStepForUser(firstUpdate.userId)
                    .orElseGet(() -> PipelineStepInfo.builder()
                            .payload("{}")
                            .pipelineName(Pipelines.CALCULATE_NUTRITION_SCORE.name())
                            .pipelineStep(Steps.CALCULATE_NUTRITION_SCORE.name())
                            .build());

            var processingResult = pipelineSteps.get(pipelineStepInfo.getFullStepName())
                    .process(pipelineStepInfo, firstUpdate);

            pipelineStepsRepository.insert(processingResult);

            processedUpdateIds.add(firstUpdate.updateId);
        });

        updatesRepository.markAsProcessed(processedUpdateIds);

        if (!processedUpdateIds.isEmpty()) {
            log.info("Processed {} updates", processedUpdateIds.size());
        }
    }
}
