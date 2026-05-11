package com.dp9v.lingua_learn_server.bot.db.repository

import com.dp9v.lingua_learn_server.bot.db.dto.BotCallbackActionDto
import com.dp9v.lingua_learn_server.bot.db.tables.BotCallbackActions
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
class BotCallbackActionRepository {

    /**
     * Create payload for a Telegram inline callback button.
     */
    fun create(botUserId: Long, action: String, payload: String?): BotCallbackActionDto {
        val id = UUID.randomUUID()
        return transaction {
            BotCallbackActions.insert {
                it[BotCallbackActions.id] = id
                it[BotCallbackActions.botUserId] = botUserId
                it[BotCallbackActions.action] = action
                it[BotCallbackActions.payload] = payload
            }

            findById(id) ?: error("Callback action was not created: $id")
        }
    }

    /**
     * Read callback payload without modifying it.
     */
    fun getById(id: UUID): BotCallbackActionDto? {
        return transaction {
            findById(id)
        }
    }

    /**
     * Mark callback payload as consumed and return it.
     * Returns null for unknown callbacks, callbacks of another user or already consumed callbacks.
     */
    fun consume(id: UUID, botUserId: Long): BotCallbackActionDto? {
        return transaction {
            val callback = BotCallbackActions.selectAll()
                .where {
                    (BotCallbackActions.id eq id) and
                            (BotCallbackActions.botUserId eq botUserId) and
                            BotCallbackActions.consumedAt.isNull()
                }
                .singleOrNull()
                ?.toDto()
                ?: return@transaction null

            val consumedAt = LocalDateTime.now()
            val updatedRows = BotCallbackActions.update({
                (BotCallbackActions.id eq id) and
                        (BotCallbackActions.botUserId eq botUserId) and
                        BotCallbackActions.consumedAt.isNull()
            }) {
                it[BotCallbackActions.consumedAt] = consumedAt
            }

            if (updatedRows == 0) {
                return@transaction null
            }

            callback.copy(consumedAt = consumedAt)
        }
    }

    private fun findById(id: UUID): BotCallbackActionDto? = BotCallbackActions.selectAll()
        .where { BotCallbackActions.id eq id }
        .singleOrNull()
        ?.toDto()

    private fun ResultRow.toDto(): BotCallbackActionDto = BotCallbackActionDto(
        id = this[BotCallbackActions.id],
        botUserId = this[BotCallbackActions.botUserId],
        action = this[BotCallbackActions.action],
        payload = this[BotCallbackActions.payload],
        createdAt = this[BotCallbackActions.createdAt],
        consumedAt = this[BotCallbackActions.consumedAt],
    )
}



