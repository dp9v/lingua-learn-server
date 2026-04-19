package com.dp9v.lingua_learn_server.bot.handlers

import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * Interface for handling incoming Telegram updates.
 */
interface UpdateHandler {
    /**
     * Checks whether this handler can process the given update.
     */
    fun canHandle(update: Update): Boolean

    /**
     * Processes the update.
     */
    fun handle(sender: AbsSender, update: Update)
}