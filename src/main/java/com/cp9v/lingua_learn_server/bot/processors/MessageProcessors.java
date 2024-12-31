package com.cp9v.lingua_learn_server.bot.processors;

import com.cp9v.lingua_learn_server.bot.client.TgClient;
import com.cp9v.lingua_learn_server.bot.client.dto.ResponseMessage;
import com.cp9v.lingua_learn_server.bot.repositories.UpdatesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProcessors {

    private final UpdatesRepository updatesRepository;
    private final TgClient tgClient;

    @Scheduled(fixedRate = 500)
    public void process() {
        var updates = updatesRepository.getAllNew();
        var processedUpdateIds = new ArrayList<Integer>();
        var failedUpdateIds = new ArrayList<Integer>();
        for (var update : updates) {
            try {
                tgClient.sendMessage(new ResponseMessage(update.chatId, "Response: %s".formatted(update.text)));
                processedUpdateIds.add(update.updateId);
            } catch (RuntimeException e) {
                failedUpdateIds.add(update.updateId);
            }
        }
        updatesRepository.markAsProcessed(processedUpdateIds);

        if (!updates.isEmpty()) {
            log.info("Processed {} updates", processedUpdateIds.size());
            log.warn("Failed {} updates", failedUpdateIds.size());
        }
    }
}
