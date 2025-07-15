CREATE SCHEMA IF NOT EXISTS medical;

CREATE TABLE IF NOT EXISTS medical.user(
    id integer primary key not null,
    email varchar(255) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    password varchar(255) not null,
    phone_number varchar(16) not null,
    created_on timestamptz not null,
    updated_on timestamptz
)