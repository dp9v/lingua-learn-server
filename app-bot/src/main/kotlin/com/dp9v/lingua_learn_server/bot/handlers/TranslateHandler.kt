package com.dp9v.lingua_learn_server.bot.handler

import com.dp9v.libretranslate.LibreTranslateClient
import com.dp9v.libretranslate.dto.LanguageCode
import com.dp9v.lingua_learn_server.bot.handlers.UpdateHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class TranslateHandler(
    private val libreTranslateClient: LibreTranslateClient
) : UpdateHandler {

    private val log = LoggerFactory.getLogger(TranslateHandler::class.java)

    companion object {
        private val SUPPORTED_SOURCES = setOf(LanguageCode.ENGLISH, LanguageCode.CZECH)
    }

    override fun canHandle(update: Update): Boolean =
        update.hasMessage()
                && update.message.hasText()
                && !update.message.text.startsWith("/")

    override fun handle(sender: AbsSender, update: Update) {
        val chatId = update.message.chatId.toString()
        val text = update.message.text.trim()

        try {
            val detections = libreTranslateClient.detect(text)
            if (detections.isNullOrEmpty()) {
                sendReply(sender, chatId, "❌ Не удалось определить язык.")
                return
            }

            val detected = detections.first().language()
            if (detected !in SUPPORTED_SOURCES) {
                sendReply(sender, chatId, "⚠️ Поддерживается перевод только с английского и чешского.")
                return
            }

            val response = libreTranslateClient.translate(text, detected, LanguageCode.RUSSIAN)
            val reply =
                "🔤 *${detected.code.uppercase()}* → *${LanguageCode.RUSSIAN.code.uppercase()}*\n\n${response.translatedText()}"
            sendReply(sender, chatId, reply)
        } catch (e: Exception) {
            log.error("Translation failed for chatId={}", chatId, e)
            sendReply(sender, chatId, "❌ Ошибка при переводе: ${e.message}")
        }
    }

    private fun sendReply(sender: AbsSender, chatId: String, text: String) {
        try {
            sender.execute(
                SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .parseMode("Markdown")
                    .build()
            )
        } catch (e: Exception) {
            log.error("Failed to send reply to chatId={}", chatId, e)
        }
    }
}