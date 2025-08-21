package com.example.med_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.tomcat.util.digester.ArrayStack;

import java.util.List;

@Data
@Entity
@Table(name = "dental_records", schema = "dental_clinic")
public class DentalRecord extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private User patient;
    private String notes;
    @OneToMany(mappedBy = "dentalRecord", fetch = FetchType.LAZY)
    private List<ToothStatus> teethStatus = new ArrayStack<>();
}
