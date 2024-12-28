create table if not exists bot__fast_updates
(
    update_id integer
        constraint bot__fast_updates_pk
            primary key,
    date      timestamp,
    json      text
);

create table if not exists bot__Users
(
    id         integer
        constraint bot__Users_pk
            primary key,
    is_bot     bool default TRUE not null,
    first_name varchar           not null,
    last_name  varchar,
    username   varchar
);

create table bot__updates
(
    update_id  integer
        constraint bot__updates_pk
            primary key,
    message_id integer not null,
    userid     integer
        constraint bot__updates_bot__users_id_fk
            references bot__users,
    date       timestamp,
    text       text
);


