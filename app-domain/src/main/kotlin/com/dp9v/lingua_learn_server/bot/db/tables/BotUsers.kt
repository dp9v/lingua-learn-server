package com.dp9v.lingua_learn_server.bot.db.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * Telegram user personal info available from Telegram API.
 * Maps to `bot__users` table.
 */
object BotUsers : Table("bot__users") {
    val id = long("id")
    val isBot = bool("is_bot").default(false)
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255).nullable()
    val username = varchar("username", 255).nullable()
    val languageCode = varchar("language_code", 10).nullable()
    val createdAt = datetime("created_at").defaultExpression(org.jetbrains.exposed.sql.javatime.CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(org.jetbrains.exposed.sql.javatime.CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}

