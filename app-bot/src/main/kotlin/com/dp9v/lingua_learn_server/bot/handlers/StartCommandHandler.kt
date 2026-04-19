package com.dp9v.lingua_learn_server.bot.handler

import com.dp9v.lingua_learn_server.bot.handlers.CommandHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class StartCommandHandler : CommandHandler {
    private val log = LoggerFactory.getLogger(StartCommandHandler::class.java)

    override val command = "/start"

    override fun handle(sender: AbsSender, update: Update) {
        val chatId = update.message.chatId.toString()
        val userName = update.message.from.firstName

        try {
            sender.execute(
                SendMessage.builder()
                    .chatId(chatId)
                    .text("Hi, $userName! 👋\nWelcome to LinguaLearn Bot!")
                    .build()
            )
        } catch (e: Exception) {
            log.error("Failed to send /start response for chatId={}", chatId, e)
        }
    }
}