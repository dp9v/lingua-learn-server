CREATE TYPE language_code AS ENUM ('en', 'ru', 'cz');

DROP TABLE IF EXISTS api__words;

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
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (word_from_id, word_to_id)
);

CREATE TABLE IF NOT EXISTS api__group_word_pairs
(
    id           SERIAL PRIMARY KEY,
    group_id     INTEGER NOT NULL REFERENCES api__groups (id) ON DELETE RESTRICT,
    word_pair_id INTEGER NOT NULL REFERENCES api__word_pairs (id) ON DELETE RESTRICT,
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (group_id, word_pair_id)
);

CREATE TABLE IF NOT EXISTS api__user_groups
(
    user_id INTEGER NOT NULL REFERENCES api__users (id) ON DELETE RESTRICT,
    group_id INTEGER NOT NULL REFERENCES api__groups (id) ON DELETE RESTRICT,
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, group_id)
)
