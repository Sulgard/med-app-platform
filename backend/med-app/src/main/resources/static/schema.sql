CREATE SCHEMA IF NOT EXISTS dental_clinic;

CREATE TYPE IF NOT EXISTS dental_clinic.gender AS ENUM ('MALE', 'FEMALE');
CREATE TYPE IF NOT EXISTS dental_clinic.role as ENUM('PATIENT', 'DOCTOR', 'RECEPTIONIST', 'HYGIENIST');
CREATE TYPE IF NOT EXISTS dental_clinic.tooth_condition as ENUM('HEALTHY', 'MISSING', 'FILLING', 'CAVITY');
CREATE TYPE IF NOT EXISTS dental_clinic.appointment_statuses as ENUM('PENDING', 'COMPLETED', 'CANCELED', 'MISSED');

CREATE TABLE IF NOT EXISTS dental_clinic.roles(
    role_id INTEGER PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS dental_clinic.privileges(
    privilege_id INTEGER PRIMARY KEY NOT NULL,
    name  VARCHAR(255) NOT NULL,
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS dental_clinic.role_privileges(
    role_id INTEGER NOT NULL REFERENCES dental_clinic.roles(role_id) ON DELETE CASCADE,
    privilage_id INTEGER NOT NULL REFERENCES dental_clinic.privileges(privilege_id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, privilage_id)
);

CREATE TABLE IF NOT EXISTS dental_clinic.user(
    user_id INTEGER PRIMARY KEY NOT NULL,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(16) NOT NULL,
    gender dental_clinic.gender NOT NULL,
    date_of_birth DATE NOT NULL,
    medical_license VARCHAR(255),
    insurance VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE,
    last_login TIMESTAMPTZ,
    role_id INTEGER REFERENCES medical.roles(role_id),
    created_on TIMESTAMPTZ NOT NULL,
    updated_on TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS dental_clinic.appointments(
    appointment_id INTEGER PRIMARY KEY NOT NULL,
    patient_id INTEGER NOT NULL,
    doctor_id INTEGER NOT NULL,
    appointment_date TIMESTAMP NOT NULL,
    status VARCHAR(100) NOT NULL,
    notes TEXT
);

ALTER TABLE dental_clinic.appointments ADD CONSTRAINT unique_appointment
UNIQUE (doctor_id, appointment_date);

CREATE TABLE IF NOT EXISTS dental_clinic.prescriptions(
    prescription_id INTEGER PRIMARY KEY NOT NULL,
    medication_name VARCHAR(255) NOT NULL,
    dosage VARCHAR(255) NOT NULL,
    instruction TEXT NOT NULL,
    prescription_code VARCHAR(255) NOT NULL,
    patient_id INTEGER NOT NULL,
    doctor_id INTEGER NOT NULL,
    expiry_date DATE NOT NULL,
    assigned_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS dental_clinic.tooth_status(
    tooth_id INTEGER PRIMARY KEY NOT NULL,
    tooth_number INTEGER NOT NULL,
    tooth_condition dental_clinic.tooth_condition NOT NULL,
    notes TEXT,
    patient_id INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS dental_clinic.dental_records(
    dental_record_id INTEGER PRIMARY KEY NOT NULL,
    patient_id INTEGER NOT NULL,
    notes TEXT
);


