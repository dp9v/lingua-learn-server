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

-- Reset sequences for API tables
ALTER SEQUENCE api__users_id_seq RESTART WITH 1;
ALTER SEQUENCE api__words_id_seq RESTART WITH 1;
ALTER SEQUENCE api__groups_id_seq RESTART WITH 1;
ALTER SEQUENCE api__word_pairs_id_seq RESTART WITH 1;
ALTER SEQUENCE api__group_word_pairs_id_seq RESTART WITH 1;


