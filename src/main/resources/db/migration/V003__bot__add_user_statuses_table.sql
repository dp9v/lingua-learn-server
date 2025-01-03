create table bot__user_statuses
(
    id                 bigserial
        constraint bot__user_statuses_pk
            primary key,
    user_id            integer                      not null
        constraint bot__user_statuses_bot__users_id_fk
            references bot__users,
    update_id          integer                      not null
        constraint bot__user_statuses_bot__updates_update_id_fk
            references bot__updates,
    pipeline_name      varchar                      not null,
    pipeline_step_name varchar                      not null,
    payload            json      default '{}'::json not null,
    created_time       timestamp default now()      not null
);

create index bot__user_statuses_user_id_created_time_index
    on public.bot__user_statuses (user_id, created_time);

