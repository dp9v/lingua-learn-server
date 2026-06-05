package com.dp9v.lingua_learn_server.bot.db.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * Link between bot users and API users (one-to-one).
 * Maps to `bot__user_api_links` table.
 */
object BotUserApiLinks : Table("bot__user_api_links") {
    val botUserId = long("bot_user_id").references(BotUsers.id)
    val apiUserId = integer("api_user_id")
    val createdAt = datetime("created_at").defaultExpression(org.jetbrains.exposed.sql.javatime.CurrentDateTime)

    override val primaryKey = PrimaryKey(botUserId, apiUserId)
}

