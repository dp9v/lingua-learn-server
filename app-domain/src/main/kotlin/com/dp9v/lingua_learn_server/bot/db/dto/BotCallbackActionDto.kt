package com.dp9v.lingua_learn_server.bot.db.dto

import java.time.LocalDateTime
import java.util.UUID

data class BotCallbackActionDto(
    val id: UUID,
    val botUserId: Long,
    val action: String,
    val payload: String?,
    val createdAt: LocalDateTime,
    val consumedAt: LocalDateTime?,
)

