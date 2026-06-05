package com.dp9v.lingua_learn_server.bot.db.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * Additional per-user settings for the bot (key-value).
 * Maps to `bot__user_settings` table.
 */
object BotUserSettings : Table("bot__user_settings") {
    val botUserId = long("bot_user_id").references(BotUsers.id)
    val key = varchar("key", 255)
    val value = text("value").nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(botUserId, key)
}

