CREATE TABLE api__words
(
    id      BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    word    TEXT NOT NULL,
    meaning TEXT
);
