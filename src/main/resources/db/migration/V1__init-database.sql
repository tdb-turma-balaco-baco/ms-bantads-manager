create sequence if not exists managers_id_seq;

create table if not exists managers
(
    id         bigint primary key,
    cpf        char(11) unique     not null,
    email      varchar(255) unique not null,
    first_name varchar(255)        not null,
    last_name  varchar(255)        not null,
    phone      varchar(16)         not null,
    is_active  bool                         default true,

    created_by varchar(255),
    updated_by varchar(255),

    created_at timestamp           not null default current_timestamp,
    updated_at timestamp           not null default current_timestamp
);

