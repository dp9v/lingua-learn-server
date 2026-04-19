package com.dp9v.lingua_learn_server.bot.handlers

import org.telegram.telegrambots.meta.api.objects.Update

/**
 * Base interface for command handlers (messages starting with "/").
 */
interface CommandHandler : UpdateHandler {
    /**
     * The command this handler processes (e.g. "/start").
     */
    val command: String

    override fun canHandle(update: Update): Boolean {
        return update.hasMessage()
                && update.message.hasText()
                && update.message.text.startsWith(this.command)
    }
}