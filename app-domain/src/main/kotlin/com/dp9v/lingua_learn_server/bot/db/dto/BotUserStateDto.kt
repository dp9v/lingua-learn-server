package com.dp9v.lingua_learn_server.bot.db.dto

data class BotUserStateDto(
    val stateName: String,
    val payload: String? = null,
)

