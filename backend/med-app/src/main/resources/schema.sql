CREATE SCHEMA IF NOT EXISTS dental_clinic;

CREATE TABLE IF NOT EXISTS dental_clinic.appointment_statuses(
    status_id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS dental_clinic.tooth_conditions(
    condition_id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS dental_clinic.roles(
    role_id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL,
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS dental_clinic.privileges(
    privilege_id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS dental_clinic.role_privileges(
    role_id INTEGER NOT NULL REFERENCES dental_clinic.roles(role_id) ON DELETE CASCADE,
    privilege_id INTEGER NOT NULL REFERENCES dental_clinic.privileges(privilege_id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, privilege_id)
);

CREATE TABLE IF NOT EXISTS dental_clinic.users(
    user_id SERIAL PRIMARY KEY NOT NULL,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(16) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    medical_license VARCHAR(255),
    insurance VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE,
    last_login TIMESTAMPTZ,
    role_id INTEGER REFERENCES dental_clinic.roles(role_id),
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS dental_clinic.refresh_tokens(
    refresh_token_id SERIAL PRIMARY KEY NOT NULL,
    user_id BIGINT UNIQUE NOT NULL REFERENCES dental_clinic.users(user_id) ON DELETE CASCADE,
    token TEXT NOT NULL,
    expiry_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS dental_clinic.appointments(
    appointment_id SERIAL PRIMARY KEY NOT NULL,
    patient_id INTEGER NOT NULL REFERENCES dental_clinic.users(user_id) ON DELETE CASCADE,
    doctor_id INTEGER NOT NULL REFERENCES dental_clinic.users(user_id) ON DELETE CASCADE,
    appointment_date TIMESTAMP NOT NULL,
    status_id INTEGER NOT NULL REFERENCES dental_clinic.appointment_statuses(status_id) ON DELETE CASCADE,
    notes TEXT,
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);

ALTER TABLE dental_clinic.appointments
DROP CONSTRAINT IF EXISTS unique_appointment;

ALTER TABLE dental_clinic.appointments
ADD CONSTRAINT unique_appointment UNIQUE (doctor_id, appointment_date);

CREATE TABLE IF NOT EXISTS dental_clinic.prescriptions(
    prescription_id SERIAL PRIMARY KEY NOT NULL,
    medication_name VARCHAR(255) NOT NULL,
    dosage VARCHAR(255) NOT NULL,
    instruction TEXT NOT NULL,
    prescription_code VARCHAR(255) NOT NULL,
    patient_id INTEGER NOT NULL REFERENCES dental_clinic.users(user_id) ON DELETE CASCADE,
    doctor_id INTEGER NOT NULL REFERENCES dental_clinic.users(user_id) ON DELETE CASCADE,
    expiry_date DATE NOT NULL,
    assigned_date DATE NOT NULL,
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS dental_clinic.dental_records(
    dental_record_id SERIAL PRIMARY KEY NOT NULL,
    patient_id INTEGER NOT NULL REFERENCES dental_clinic.users(user_id) ON DELETE CASCADE,
    notes TEXT,
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS dental_clinic.tooth_status(
    tooth_id SERIAL PRIMARY KEY NOT NULL,
    tooth_number INTEGER NOT NULL,
    tooth_condition INTEGER NOT NULL REFERENCES dental_clinic.tooth_conditions(condition_id) ON DELETE CASCADE,
    notes TEXT,
    patient_id INTEGER NOT NULL REFERENCES dental_clinic.users(user_id) ON DELETE CASCADE,
    dental_record_id INTEGER NOT NULL REFERENCES dental_clinic.dental_records(dental_record_id)
    ON DELETE CASCADE,
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);
