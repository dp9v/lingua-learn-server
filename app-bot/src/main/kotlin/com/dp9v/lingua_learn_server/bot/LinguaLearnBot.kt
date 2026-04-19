package com.dp9v.lingua_learn_server.bot

import com.dp9v.lingua_learn_server.bot.config.BotProperties
import com.dp9v.lingua_learn_server.bot.handlers.UpdateHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class LinguaLearnBot(
    private val botProperties: BotProperties,
    private val handlers: List<UpdateHandler>
) : TelegramLongPollingBot(botProperties.token) {
    private val log = LoggerFactory.getLogger(LinguaLearnBot::class.java)

    override fun getBotUsername(): String = botProperties.name

    override fun onUpdateReceived(update: Update) {
        log.debug("Received update: {}", update.updateId)
        handlers.firstOrNull { it.canHandle(update) }
            ?.handle(this, update)
            ?: log.debug("No suitable handler for update {}", update.updateId)
    }
}