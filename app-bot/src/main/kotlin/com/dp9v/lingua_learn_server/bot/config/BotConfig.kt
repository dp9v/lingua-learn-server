package com.dp9v.lingua_learn_server.bot.config

import com.dp9v.lingua_learn_server.bot.LinguaLearnBot
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Configuration
class BotConfig {
    private val log = LoggerFactory.getLogger(BotConfig::class.java)

    @Bean
    fun telegramBotsApi(bot: LinguaLearnBot): TelegramBotsApi {
        val api = TelegramBotsApi(DefaultBotSession::class.java)
        api.registerBot(bot)
        log.info("Telegram bot '{}' registered successfully", bot.botUsername)
        return api
    }
}