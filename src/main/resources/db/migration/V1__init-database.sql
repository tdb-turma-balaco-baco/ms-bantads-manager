create table if not exists managers
(
    id           uuid primary key,
    cpf          char(11) unique     not null,
    email        varchar(255) unique not null,
    name         varchar(255)        not null,
    phone_number varchar(16)         not null,
    accounts     integer             not null default 0
);
