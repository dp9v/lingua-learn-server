-- =============================================
-- Bot user tables (prefix: bot__)
-- =============================================

-- Telegram user personal info available from Telegram API
CREATE TABLE IF NOT EXISTS bot__users
(
    id            BIGINT PRIMARY KEY,            -- Telegram user id
    is_bot        BOOLEAN   NOT NULL DEFAULT FALSE,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255),
    username      VARCHAR(255),
    language_code VARCHAR(10),                   -- IETF language tag from Telegram
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Link between bot users and API users (one-to-one)
CREATE TABLE IF NOT EXISTS bot__user_api_links
(
    bot_user_id BIGINT  NOT NULL REFERENCES bot__users (id) ON DELETE CASCADE,
    api_user_id INTEGER NOT NULL REFERENCES api__users (id) ON DELETE CASCADE,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (bot_user_id, api_user_id),
    CONSTRAINT uq_bot_user_api_link UNIQUE (bot_user_id),
    CONSTRAINT uq_api_user_bot_link UNIQUE (api_user_id)
);

-- Current state of a bot user in the bot state graph
CREATE TABLE IF NOT EXISTS bot__user_states
(
    bot_user_id   BIGINT       PRIMARY KEY REFERENCES bot__users (id) ON DELETE CASCADE,
    state_name    VARCHAR(255) NOT NULL,          -- current node in the state graph
    payload       TEXT,                            -- arbitrary JSON data associated with the state
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Additional per-user settings for the bot
CREATE TABLE IF NOT EXISTS bot__user_settings
(
    bot_user_id      BIGINT       NOT NULL REFERENCES bot__users (id) ON DELETE CASCADE,
    key              VARCHAR(255) NOT NULL,
    value            TEXT,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (bot_user_id, key)
);






