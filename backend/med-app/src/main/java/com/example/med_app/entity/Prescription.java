package com.example.med_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "prescriptions", schema = "dental_clinic")
public class Prescription extends BaseEntity {
    private String medicationName;
    private String dosage;
    private String instruction;
    private String prescriptionCode;
    @Column(name = "patient_id")
    private User patient;
    @Column(name = "doctor_id")
    private User doctor;
    private LocalDate expiryDate;
    private LocalDate assignedDate;
}
