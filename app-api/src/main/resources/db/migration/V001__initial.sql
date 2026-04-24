CREATE TYPE language_code AS ENUM ('EN', 'RU', 'CZ');

CREATE TABLE IF NOT EXISTS api__users
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(100) NOT NULL,
    surname    VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT email_idx UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS api__words
(
    id         SERIAL PRIMARY KEY,
    language   language_code NOT NULL,
    text       TEXT          NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT words_lang_idx UNIQUE (text, language)
);

CREATE TABLE IF NOT EXISTS api__groups
(
    id          SERIAL PRIMARY KEY,
    name        TEXT    NOT NULL,
    description TEXT    NOT NULL,
    is_public   BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS api__word_pairs
(
    id           SERIAL PRIMARY KEY,
    word_from_id INTEGER NOT NULL REFERENCES api__words (id) ON DELETE RESTRICT,
    word_to_id   INTEGER NOT NULL REFERENCES api__words (id) ON DELETE RESTRICT,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (word_from_id, word_to_id)
);

CREATE TABLE IF NOT EXISTS api__group_word_pairs
(
    id           SERIAL PRIMARY KEY,
    group_id     INTEGER NOT NULL REFERENCES api__groups (id) ON DELETE RESTRICT,
    word_pair_id INTEGER NOT NULL REFERENCES api__word_pairs (id) ON DELETE RESTRICT,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (group_id, word_pair_id)
);

CREATE TABLE IF NOT EXISTS api__user_groups
(
    user_id    INTEGER NOT NULL REFERENCES api__users (id) ON DELETE RESTRICT,
    group_id   INTEGER NOT NULL REFERENCES api__groups (id) ON DELETE RESTRICT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, group_id)
);

CREATE TABLE IF NOT EXISTS bot__fast_updates
(
    update_id INTEGER CONSTRAINT bot__fast_updates_pk PRIMARY KEY,
    date      TIMESTAMP,
    json      TEXT
);

CREATE TABLE IF NOT EXISTS bot__users
(
    id         INTEGER CONSTRAINT bot__users_pk PRIMARY KEY,
    is_bot     BOOL DEFAULT TRUE NOT NULL,
    first_name VARCHAR           NOT NULL,
    last_name  VARCHAR,
    username   VARCHAR
);

CREATE TABLE IF NOT EXISTS bot__updates
(
    update_id  INTEGER CONSTRAINT bot__updates_pk PRIMARY KEY,
    message_id INTEGER               NOT NULL,
    chat_id    INTEGER               NOT NULL,
    user_id    INTEGER CONSTRAINT bot__updates_bot__users_id_fk REFERENCES bot__users,
    date       TIMESTAMP,
    text       TEXT,
    status     VARCHAR DEFAULT 'NEW' NOT NULL
);

CREATE TABLE IF NOT EXISTS bot__user_statuses
(
    id                 BIGSERIAL CONSTRAINT bot__user_statuses_pk PRIMARY KEY,
    user_id            INTEGER                      NOT NULL CONSTRAINT bot__user_statuses_bot__users_id_fk REFERENCES bot__users,
    update_id          INTEGER                      NOT NULL CONSTRAINT bot__user_statuses_bot__updates_update_id_fk REFERENCES bot__updates,
    pipeline_name      VARCHAR                      NOT NULL,
    pipeline_step_name VARCHAR                      NOT NULL,
    payload            JSON      DEFAULT '{}'::JSON NOT NULL,
    created_time       TIMESTAMP DEFAULT NOW()      NOT NULL
);

CREATE INDEX IF NOT EXISTS bot__user_statuses_user_id_created_time_index
    ON bot__user_statuses (user_id, created_time);
