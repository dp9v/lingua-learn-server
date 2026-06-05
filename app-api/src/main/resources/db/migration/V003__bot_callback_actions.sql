-- =============================================
-- Bot callback actions (prefix: bot__)
-- =============================================

-- Payload storage for Telegram inline callback buttons.
-- Telegram callback_data contains only a short reference to this table.
CREATE TABLE IF NOT EXISTS bot__callback_actions
(
    id          UUID PRIMARY KEY,
    bot_user_id BIGINT       NOT NULL REFERENCES bot__users (id) ON DELETE CASCADE,
    action      VARCHAR(255) NOT NULL,
    payload     TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    consumed_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_bot_callback_actions_bot_user_id
    ON bot__callback_actions (bot_user_id);

CREATE INDEX IF NOT EXISTS idx_bot_callback_actions_consumed_at
    ON bot__callback_actions (consumed_at);

