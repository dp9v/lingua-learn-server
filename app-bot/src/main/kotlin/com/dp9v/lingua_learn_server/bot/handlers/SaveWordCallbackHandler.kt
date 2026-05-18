package com.dp9v.lingua_learn_server.bot.handler

import com.dp9v.lingua_learn_server.bot.callback.BotCallbackData
import com.dp9v.lingua_learn_server.bot.callback.PendingTranslation
import com.dp9v.lingua_learn_server.bot.callback.SaveTranslationCallbackAction
import com.dp9v.lingua_learn_server.bot.db.repository.BotCallbackActionRepository
import com.dp9v.lingua_learn_server.bot.handlers.UpdateHandler
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class SaveWordCallbackHandler(
    private val botCallbackActionRepository: BotCallbackActionRepository,
) : UpdateHandler {

    private val log = LoggerFactory.getLogger(SaveWordCallbackHandler::class.java)
    private val objectMapper = jacksonObjectMapper()

    override fun canHandle(update: Update): Boolean =
        update.hasCallbackQuery()
                && update.callbackQuery.data != null
                && BotCallbackData.unpack(update.callbackQuery.data) != null

    override fun handle(sender: AbsSender, update: Update) {
        val callback = update.callbackQuery
        val chatId = callback.message.chatId.toString()
        val messageId = callback.message.messageId
        val callbackId = BotCallbackData.unpack(callback.data)
        val botUserId = callback.from.id

        try {
            if (callbackId == null) {
                answerCallback(sender, callback.id)
                return
            }

            val callbackAction = botCallbackActionRepository.consume(callbackId, botUserId)
            val payload = callbackAction?.payload

            if (callbackAction == null || payload == null) {
                editMessage(sender, chatId, messageId, "⏰ Данные устарели. Попробуйте перевести слово заново.")
                answerCallback(sender, callback.id)
                return
            }

            val action = SaveTranslationCallbackAction.fromActionName(callbackAction.action)
            if (action == null) {
                editMessage(sender, chatId, messageId, "⚠️ Неизвестное callback-действие.")
                answerCallback(sender, callback.id)
                return
            }

            val pending = objectMapper.readValue<PendingTranslation>(payload)

            when (action) {
                SaveTranslationCallbackAction.SAVE -> {
                    // TODO: persist the word pair via API
                    log.info(
                        "User {} saved word pair: {} ({}) -> {} ({})",
                        botUserId,
                        pending.sourceText, pending.sourceLang,
                        pending.translatedText, pending.targetLang
                    )
                    editMessage(
                        sender, chatId, messageId,
                        "✅ Сохранено!\n\n*${pending.sourceText}* → *${pending.translatedText}*"
                    )
                }

                SaveTranslationCallbackAction.CANCEL -> {
                    editMessage(
                        sender, chatId, messageId,
                        "❌ Отменено.\n\n~${pending.sourceText} → ${pending.translatedText}~"
                    )
                }
            }
            answerCallback(sender, callback.id)
        } catch (e: Exception) {
            log.error("Failed to handle callback for chatId={}", chatId, e)
        }
    }

    private fun editMessage(sender: AbsSender, chatId: String, messageId: Int, text: String) {
        sender.execute(
            EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .parseMode("Markdown")
                .build()
        )
    }

    private fun answerCallback(sender: AbsSender, callbackQueryId: String) {
        sender.execute(
            AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .build()
        )
    }
}
