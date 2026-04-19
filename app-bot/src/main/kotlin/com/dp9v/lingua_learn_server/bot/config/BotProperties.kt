package com.dp9v.lingua_learn_server.bot.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bot")
class BotProperties(
    val name: String,
    val token: String
) {
    init {
        require(name.isNotEmpty()) {
            "Name must not be empty"
        }
        require(token.isNotEmpty()) {
            "Token must not be empty"
        }
    }
}
