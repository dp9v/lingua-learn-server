package com.cp9v.lingua_learn_server.bot.client;

import com.cp9v.lingua_learn_server.bot.client.clients.TgClient;
import com.cp9v.lingua_learn_server.bot.client.dto.FastUpdate;
import com.cp9v.lingua_learn_server.bot.client.repositories.FastUpdatesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BotPoller {

    private final FastUpdatesRepository updatesRepository;
    private final TgClient tgClient;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Scheduled(fixedDelay = 1000)
    public void poll() {
        var offset = updatesRepository.getLastUpdateId();
        var updates = tgClient.getUpdates(offset + 1);
        var fastUpdates = new ArrayList<FastUpdate>();
        for (var update : updates) {
            fastUpdates.add(FastUpdate.builder()
                    .updateId(update.id)
                    .date(update.message.date)
                    .json(objectMapper.writeValueAsString(update))
                    .build());
        }
        updatesRepository.create(fastUpdates);
    }
}
