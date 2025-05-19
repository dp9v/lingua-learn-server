INSERT INTO api__users (id, email, password, name, surname)
VALUES (1001,
        'test@test.com',
        '$2a$10$1zQsnW.Pf8z1lTOS2qEUg.sr9DPoZELQKZclrwiHBI26hCVxx6SJq',
        'testName',
        'testSurname');

INSERT INTO api__words (id, language, text)
VALUES (101, 'en'::language_code, 'word_1'),
       (102, 'en'::language_code, 'word_2'),
       (103, 'en'::language_code, 'word_3'),
       (201, 'ru'::language_code, 'слово_1'),
       (202, 'ru'::language_code, 'слово_2'),
       (203, 'ru'::language_code, 'слово_3'),
       (301, 'cz'::language_code, 'slovo_1'),
       (302, 'cz'::language_code, 'slovo_2'),
       (303, 'cz'::language_code, 'slovo_3');

INSERT INTO api__groups(id, name, description, is_public)
VALUES (101, 'Eng to Rus', 'Англо-русский', true),
       (201, 'Rus to Czech', 'Русско-чешский', true),
       (301, 'Czech to Eng', 'Чешско-английский', false);

INSERT INTO api__word_pairs(id, word_from_id, word_to_id)
VALUES (101, 101, 201),
       (102, 102, 202),
       (103, 103, 203),
       (201, 201, 301),
       (202, 202, 302),
       (203, 203, 303),
       (301, 301, 101),
       (302, 302, 102),
       (303, 303, 103);

INSERT INTO api__group_word_pairs(group_id, word_pair_id)
VALUES (101, 101),
       (101, 102),
       (101, 103),
       (201, 201),
       (201, 202),
       (201, 203),
       (301, 301),
       (301, 302),
       (301, 303);