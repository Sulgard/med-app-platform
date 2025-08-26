package com.example.med_app.entity;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;
    private LocalDate expiryDate;
    private LocalDate assignedDate;
}
