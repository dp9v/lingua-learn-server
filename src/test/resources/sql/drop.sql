-- Clean API tables in the correct order (respecting foreign key constraints)
DELETE
FROM api__user_groups;
DELETE
FROM api__group_word_pairs;
DELETE
FROM api__word_pairs;
DELETE
FROM api__groups;
DELETE
FROM api__words;
DELETE
FROM api__users;

-- Clean BOT tables in the correct order (respecting foreign key constraints)
DELETE
FROM bot__user_statuses;
DELETE
FROM bot__updates;
DELETE
FROM bot__users;
DELETE
FROM bot__fast_updates;

-- Reset sequences for API tables
ALTER SEQUENCE api__users_id_seq RESTART WITH 1;
ALTER SEQUENCE api__words_id_seq RESTART WITH 1;
ALTER SEQUENCE api__groups_id_seq RESTART WITH 1;
ALTER SEQUENCE api__word_pairs_id_seq RESTART WITH 1;
ALTER SEQUENCE api__group_word_pairs_id_seq RESTART WITH 1;

-- Reset sequences for BOT tables
ALTER SEQUENCE bot__user_statuses_id_seq RESTART WITH 1;

