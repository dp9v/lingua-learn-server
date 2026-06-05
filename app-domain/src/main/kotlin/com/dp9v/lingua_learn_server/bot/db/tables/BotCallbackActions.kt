package com.dp9v.lingua_learn_server.bot.db.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * Payload storage for Telegram inline callback buttons.
 * Maps to `bot__callback_actions` table.
 */
object BotCallbackActions : Table("bot__callback_actions") {
    val id = uuid("id")
    val botUserId = long("bot_user_id").references(BotUsers.id)
    val action = varchar("action", 255)
    val payload = text("payload").nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val consumedAt = datetime("consumed_at").nullable()

    override val primaryKey = PrimaryKey(id)
}

