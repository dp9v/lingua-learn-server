package com.dp9v.lingua_learn_server.bot.db.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * Current state of a bot user in the bot state graph.
 * Maps to `bot__user_states` table.
 */
object BotUserStates : Table("bot__user_states") {
    val botUserId = long("bot_user_id").references(BotUsers.id)
    val stateName = varchar("state_name", 255)
    val payload = text("payload").nullable()
    val createdAt = datetime("created_at").defaultExpression(org.jetbrains.exposed.sql.javatime.CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(org.jetbrains.exposed.sql.javatime.CurrentDateTime)

    override val primaryKey = PrimaryKey(botUserId)
}
