package com.cp9v.lingua_learn_server.bot.processors;

import com.cp9v.lingua_learn_server.bot.dto.FastUpdate;
import com.cp9v.lingua_learn_server.bot.dto.Update;
import com.cp9v.lingua_learn_server.bot.repositories.FastUpdatesRepository;
import com.cp9v.lingua_learn_server.bot.repositories.UpdatesRepository;
import com.cp9v.lingua_learn_server.bot.repositories.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FastUpdatesProcessor {

    public final FastUpdatesRepository fastUpdatesRepository;
    public final UpdatesRepository updatesRepository;
    public final UsersRepository usersRepository;
    public final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 500)
    public void process() {
        var updates = fastUpdatesRepository.getUnprocessedUpdates()
                .stream()
                .map(this::unmarshal)
                .toList();

        saveNewUsers(updates);
        updatesRepository.save(updates);
    }

    private void saveNewUsers(List<Update> updates) {
        var users = updates.stream()
                .map(u -> u.message.from)
                .distinct()
                .toList();
        usersRepository.saveAllIfNotExist(users);
    }

    @SneakyThrows
    private Update unmarshal(FastUpdate update) {
        return objectMapper.readValue(update.json, Update.class);
    }
}
