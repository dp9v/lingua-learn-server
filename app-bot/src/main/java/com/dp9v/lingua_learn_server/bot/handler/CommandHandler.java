package com.dp9v.lingua_learn_server.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Base interface for command handlers (messages starting with "/").
 */
public interface CommandHandler extends UpdateHandler {

    /**
     * The command this handler processes (e.g. "/start").
     */
    String getCommand();

    @Override
    default boolean canHandle(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().startsWith(getCommand());
    }
}


