package com.example.med_app.entity;

import com.example.med_app.enums.AppointmentStatusType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
@Entity
@Table(name = "appointments", schema = "dental_clinic")
public class Appointment extends BaseEntityAudit {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private User patient;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User doctor;
    @Column(name = "appointment_date")
    private LocalDateTime appointmentDate;
    @Column(name = "status")
    private AppointmentStatusType appointmentStatus;
    private String notes;
}
