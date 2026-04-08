package com.dp9v.lingua_learn_server.bot.processors;

import com.dp9v.lingua_learn_server.bot.client.TgClient;
import com.dp9v.lingua_learn_server.bot.dto.FastUpdate;
import com.dp9v.lingua_learn_server.bot.repositories.FastUpdatesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BotUpdatesProcessor {

    private final FastUpdatesRepository fastUpdatesRepository;
    private final TgClient tgClient;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Scheduled(fixedDelay = 1000)
    public void poll() {
        var offset = fastUpdatesRepository.getLastUpdateId();
        var updates = tgClient.getUpdates(offset + 1);
        var fastUpdates = new ArrayList<FastUpdate>();
        for (var update : updates) {
            fastUpdates.add(FastUpdate.builder()
                    .updateId(update.id)
                    .date(update.message.date)
                    .json(objectMapper.writeValueAsString(update))
                    .build());
        }
        fastUpdatesRepository.save(fastUpdates);
    }
}
