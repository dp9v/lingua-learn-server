package com.dp9v.lingua_learn_server.bot.callback

import java.util.UUID

object BotCallbackData {
    private const val PREFIX = "cb:"

    fun pack(id: UUID): String = "$PREFIX$id"

    fun unpack(data: String?): UUID? {
        if (data == null || !data.startsWith(PREFIX)) {
            return null
        }

        return runCatching { UUID.fromString(data.removePrefix(PREFIX)) }.getOrNull()
    }
}

