CREATE SCHEMA IF NOT EXISTS dental;

CREATE TYPE IF NOT EXISTS dental.gender AS ENUM ('MALE', 'FEMALE');

CREATE TABLE IF NOT EXISTS dental.roles(
    role_id INTEGER PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS dental.privileges(
    privilege_id INTEGER PRIMARY KEY NOT NULL,
    name  VARCHAR(255) NOT NULL,
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS dental.role_privileges(
    role_id INTEGER NOT NULL REFERENCES dental.roles(role_id) ON DELETE CASCADE,
    privilage_id INTEGER NOT NULL REFERENCES dental.privileges(privilege_id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, privilage_id)
);

CREATE TABLE IF NOT EXISTS dental.user(
    user_id INTEGER PRIMARY KEY NOT NULL,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(16) NOT NULL,
    gender medical.gender NOT NULL,
    date_of_birth DATE NOT NULL,
    medical_license VARCHAR(255),
    insurance VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE,
    last_login TIMESTAMPTZ,
    role_id INTEGER REFERENCES medical.roles(role_id),
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);