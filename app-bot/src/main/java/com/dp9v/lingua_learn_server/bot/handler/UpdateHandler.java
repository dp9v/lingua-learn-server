package com.dp9v.lingua_learn_server.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Interface for handling incoming Telegram updates.
 */
public interface UpdateHandler {

    /**
     * Checks whether this handler can process the given update.
     */
    boolean canHandle(Update update);

    /**
     * Processes the update.
     */
    void handle(AbsSender sender, Update update);
}

