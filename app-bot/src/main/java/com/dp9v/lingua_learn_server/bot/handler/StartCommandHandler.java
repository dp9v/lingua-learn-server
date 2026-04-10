package com.dp9v.lingua_learn_server.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class StartCommandHandler implements CommandHandler {

    @Override
    public String getCommand() {
        return "/start";
    }

    @Override
    public void handle(AbsSender sender, Update update) {
        var chatId = update.getMessage().getChatId().toString();
        var userName = update.getMessage().getFrom().getFirstName();

        var message = SendMessage.builder()
                .chatId(chatId)
                .text("Hi, %s! 👋\nWelcome to LinguaLearn Bot!".formatted(userName))
                .build();

        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send /start response for chatId={}", chatId, e);
        }
    }
}


