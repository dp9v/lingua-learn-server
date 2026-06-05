package com.dp9v.lingua_learn_server.bot.handler

import com.dp9v.libretranslate.LibreTranslateClient
import com.dp9v.libretranslate.dto.LanguageCode
import com.dp9v.lingua_learn_server.bot.callback.BotCallbackData
import com.dp9v.lingua_learn_server.bot.callback.PendingTranslation
import com.dp9v.lingua_learn_server.bot.callback.SaveTranslationCallbackAction
import com.dp9v.lingua_learn_server.bot.db.dto.BotUserDto
import com.dp9v.lingua_learn_server.bot.db.repository.BotCallbackActionRepository
import com.dp9v.lingua_learn_server.bot.db.repository.BotUserRepository
import com.dp9v.lingua_learn_server.bot.handlers.UpdateHandler
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class TranslateHandler(
    private val libreTranslateClient: LibreTranslateClient,
    private val botUserRepository: BotUserRepository,
    private val botCallbackActionRepository: BotCallbackActionRepository,
) : UpdateHandler {

    private val log = LoggerFactory.getLogger(TranslateHandler::class.java)
    private val objectMapper = jacksonObjectMapper()

    companion object {
        private val SUPPORTED_LANGUAGES = setOf(LanguageCode.RUSSIAN, LanguageCode.CZECH)
    }

    override fun canHandle(update: Update): Boolean =
        update.hasMessage()
                && update.message.hasText()
                && !update.message.text.startsWith("/")

    override fun handle(sender: AbsSender, update: Update) {
        val chatId = update.message.chatId.toString()
        val text = update.message.text.trim()
        val tgUser = update.message.from

        try {
            botUserRepository.ensureUser(
                BotUserDto(
                    id = tgUser.id,
                    isBot = tgUser.isBot,
                    firstName = tgUser.firstName,
                    lastName = tgUser.lastName,
                    username = tgUser.userName,
                    languageCode = tgUser.languageCode,
                )
            )

            val detections = libreTranslateClient.detect(text)
            if (detections.isNullOrEmpty()) {
                sendReply(sender, chatId, "❌ Не удалось определить язык.")
                return
            }

            val detected = detections.first().language()
            if (detected !in SUPPORTED_LANGUAGES) {
                sendReply(sender, chatId, "⚠️ Поддерживается перевод только между русским и чешским языками.")
                return
            }

            val targetLang = if (detected == LanguageCode.RUSSIAN) LanguageCode.CZECH else LanguageCode.RUSSIAN

            val response = libreTranslateClient.translate(text, detected, targetLang)
            val translatedText = response.translatedText()

            val pending = PendingTranslation(
                sourceText = text,
                sourceLang = detected.code,
                translatedText = translatedText,
                targetLang = targetLang.code,
            )
            val callbackPayload = objectMapper.writeValueAsString(pending)
            val saveCallback = botCallbackActionRepository.create(
                botUserId = tgUser.id,
                action = SaveTranslationCallbackAction.SAVE.actionName,
                payload = callbackPayload,
            )
            val cancelCallback = botCallbackActionRepository.create(
                botUserId = tgUser.id,
                action = SaveTranslationCallbackAction.CANCEL.actionName,
                payload = callbackPayload,
            )

            val reply = "🔤 *${langName(detected)}* → *${langName(targetLang)}*\n\n" +
                    "*${text}* → *${translatedText}*\n\n" +
                    "Сохранить эту пару слов?"

            val saveButton = InlineKeyboardButton.builder()
                .text("✅ Сохранить")
                .callbackData(BotCallbackData.pack(saveCallback.id))
                .build()
            val cancelButton = InlineKeyboardButton.builder()
                .text("❌ Отмена")
                .callbackData(BotCallbackData.pack(cancelCallback.id))
                .build()
            val keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(listOf(saveButton, cancelButton))
                .build()

            sender.execute(
                SendMessage.builder()
                    .chatId(chatId)
                    .text(reply)
                    .parseMode("Markdown")
                    .replyMarkup(keyboard)
                    .build()
            )
        } catch (e: Exception) {
            log.error("Translation failed for chatId={}", chatId, e)
            sendReply(sender, chatId, "❌ Ошибка при переводе: ${e.message}")
        }
    }

    private fun langName(lang: LanguageCode): String = when (lang) {
        LanguageCode.RUSSIAN -> "RU 🇷🇺"
        LanguageCode.CZECH -> "CS 🇨🇿"
        else -> lang.code.uppercase()
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
