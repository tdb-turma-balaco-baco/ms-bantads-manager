create sequence if not exists managers_id_seq;

create table if not exists managers
(
    id                 serial primary key,
    cpf                char(11) unique     not null,
    email              varchar(255) unique not null,
    first_name         varchar(255)        not null,
    last_name          varchar(255)        not null,
    phone              varchar(16)         not null,
    accounts           integer             not null default 0,

    is_active          bool                         default true,

    created_by         varchar(255),
    last_modified_by   varchar(255),

    creation_date      timestamp           not null default current_timestamp,
    last_modified_date timestamp           not null default current_timestamp
);

