package com.dp9v.lingua_learn_server.bot;

import com.dp9v.lingua_learn_server.bot.config.BotProperties;
import com.dp9v.lingua_learn_server.bot.handler.UpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
public class LinguaLearnBot extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final List<UpdateHandler> handlers;

    public LinguaLearnBot(BotProperties botProperties, List<UpdateHandler> handlers) {
        super(botProperties.getToken());
        this.botProperties = botProperties;
        this.handlers = handlers;
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Received update: {}", update.getUpdateId());

        handlers.stream()
                .filter(handler -> handler.canHandle(update))
                .findFirst()
                .ifPresentOrElse(
                        handler -> handler.handle(this, update),
                        () -> log.debug("No suitable handler for update {}", update.getUpdateId())
                );
    }
}


