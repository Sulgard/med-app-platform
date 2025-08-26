INSERT INTO dental_clinic.roles (name, created_on) VALUES
                                                       ('PATIENT', now()),
                                                       ('NURSE', now()),
                                                       ('DOCTOR', now()),
                                                       ('RECEPTIONIST', now()),
                                                       ('ADMIN', now());

INSERT INTO dental_clinic.privileges (name, created_on) VALUES
                                                            ('VIEW_APPOINTMENTS', now()),
                                                            ('CREATE_APPOINTMENTS', now()),
                                                            ('CANCEL_APPOINTMENTS', now()),
                                                            ('VIEW_PATIENTS', now()),
                                                            ('EDIT_PATIENTS', now()),
                                                            ('VIEW_MEDICAL_RECORDS', now()),
                                                            ('EDIT_MEDICAL_RECORDS', now()),
                                                            ('MANAGE_USERS', now()),
                                                            ('MANAGE_ROLES', now());


INSERT INTO dental_clinic.role_privileges (role_id, privilege_id)
SELECT r.id, p.id
FROM dental_clinic.roles r, dental_clinic.privileges p
WHERE r.name = 'PATIENT' AND p.name IN ('VIEW_APPOINTMENTS', 'CREATE_APPOINTMENTS', 'CANCEL_APPOINTMENTS');

INSERT INTO dental_clinic.role_privileges (role_id, privilege_id)
SELECT r.id, p.id
FROM dental_clinic.roles r, dental_clinic.privileges p
WHERE r.name = 'NURSE' AND p.name IN ('VIEW_APPOINTMENTS', 'VIEW_PATIENTS', 'VIEW_MEDICAL_RECORDS');

INSERT INTO dental_clinic.role_privileges (role_id, privilege_id)
SELECT r.id, p.id
FROM dental_clinic.roles r, dental_clinic.privileges p
WHERE r.name = 'DOCTOR' AND p.name IN ('VIEW_APPOINTMENTS', 'VIEW_PATIENTS', 'EDIT_PATIENTS', 'VIEW_MEDICAL_RECORDS', 'EDIT_MEDICAL_RECORDS');

INSERT INTO dental_clinic.role_privileges (role_id, privilege_id)
SELECT r.id, p.id
FROM dental_clinic.roles r, dental_clinic.privileges p
WHERE r.name = 'RECEPTIONIST' AND p.name IN ('VIEW_APPOINTMENTS', 'CREATE_APPOINTMENTS', 'CANCEL_APPOINTMENTS', 'VIEW_PATIENTS');

INSERT INTO dental_clinic.role_privileges (role_id, privilege_id)
SELECT r.id, p.id
FROM dental_clinic.roles r, dental_clinic.privileges p
WHERE r.name = 'ADMIN';
