package com.dp9v.lingua_learn_server.bot.db.repository

import com.dp9v.lingua_learn_server.bot.db.dto.BotUserDto
import com.dp9v.lingua_learn_server.bot.db.dto.BotUserStateDto
import com.dp9v.lingua_learn_server.bot.db.tables.BotUsers
import com.dp9v.lingua_learn_server.bot.db.tables.BotUserStates
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BotUserRepository {

    /**
     * Ensure the bot user exists in bot__users (insert or update).
     */
    fun ensureUser(user: BotUserDto) {
        transaction {
            val exists = BotUsers.selectAll().where { BotUsers.id eq user.id }.singleOrNull()
            if (exists == null) {
                BotUsers.insert {
                    it[id] = user.id
                    it[isBot] = user.isBot
                    it[firstName] = user.firstName
                    it[lastName] = user.lastName
                    it[username] = user.username
                    it[languageCode] = user.languageCode
                }
            } else {
                BotUsers.update({ BotUsers.id eq user.id }) {
                    it[firstName] = user.firstName
                    it[lastName] = user.lastName
                    it[username] = user.username
                    it[languageCode] = user.languageCode
                    it[updatedAt] = LocalDateTime.now()
                }
            }
        }
    }

    /**
     * Save or update the user's current state with a payload.
     */
    fun setState(botUserId: Long, stateName: String, payload: String?) {
        transaction {
            val exists = BotUserStates.selectAll()
                .where { BotUserStates.botUserId eq botUserId }
                .singleOrNull()

            if (exists == null) {
                BotUserStates.insert {
                    it[BotUserStates.botUserId] = botUserId
                    it[BotUserStates.stateName] = stateName
                    it[BotUserStates.payload] = payload
                }
            } else {
                BotUserStates.update({ BotUserStates.botUserId eq botUserId }) {
                    it[BotUserStates.stateName] = stateName
                    it[BotUserStates.payload] = payload
                    it[updatedAt] = LocalDateTime.now()
                }
            }
        }
    }

    /**
     * Get the user's current state, or null if not set.
     */
    fun getState(botUserId: Long): BotUserStateDto? {
        return transaction {
            BotUserStates.selectAll()
                .where { BotUserStates.botUserId eq botUserId }
                .singleOrNull()
                ?.let {
                    BotUserStateDto(
                        stateName = it[BotUserStates.stateName],
                        payload = it[BotUserStates.payload],
                    )
                }
        }
    }

    /**
     * Clear the user's state.
     */
    fun clearState(botUserId: Long) {
        transaction {
            BotUserStates.deleteWhere { BotUserStates.botUserId eq botUserId }
        }
    }
}


