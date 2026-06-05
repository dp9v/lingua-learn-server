package com.dp9v.lingua_learn_server.bot.db.dto

data class BotUserDto(
    val id: Long,
    val isBot: Boolean = false,
    val firstName: String,
    val lastName: String? = null,
    val username: String? = null,
    val languageCode: String? = null,
)

