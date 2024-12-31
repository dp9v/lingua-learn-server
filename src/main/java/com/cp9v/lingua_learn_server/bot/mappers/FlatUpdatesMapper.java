package com.cp9v.lingua_learn_server.bot.mappers;

import com.cp9v.lingua_learn_server.bot.client.dto.Update;
import com.cp9v.lingua_learn_server.bot.dto.FastUpdate;
import com.cp9v.lingua_learn_server.bot.dto.FlatUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FlatUpdatesMapper {

    public FlatUpdate map(Update update) {
        return FlatUpdate.builder()
                .updateId(update.id)
                .messageId(update.message.id)
                .chatId(update.message.chat.id)
                .userId(update.message.from.id)
                .date(LocalDateTime.ofEpochSecond(update.message.date, 0, ZoneOffset.UTC))
                .text(update.message.text)
                .build();
    }


    public List<FlatUpdate> map(List<Update> update) {
        return update.stream()
                .map(this::map)
                .toList();
    }


}
