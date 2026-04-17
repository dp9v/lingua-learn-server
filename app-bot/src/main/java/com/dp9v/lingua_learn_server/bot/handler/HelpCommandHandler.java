package com.dp9v.lingua_learn_server.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class HelpCommandHandler implements CommandHandler {

    @Override
    public String getCommand() {
        return "/help";
    }

    @Override
    public void handle(AbsSender sender, Update update) {
        var chatId = update.getMessage().getChatId().toString();

        var text = """
                📚 Available commands:
                /start — start working with the bot
                /help — show help
                
                🔤 Send any word in English or Czech to get a Russian translation.
                """;

        var message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send /help response for chatId={}", chatId, e);
        }
    }
}


