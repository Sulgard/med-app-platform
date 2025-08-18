package com.example.med_app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "dental_records", schema = "dental_clinic")
public class DentalRecord extends BaseEntity {
    private User patient;
}
