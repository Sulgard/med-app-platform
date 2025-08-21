package com.example.med_app.entity;

import com.example.med_app.enums.ToothCondition;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tooth_status", schema = "dental_clinic")
public class ToothStatus extends BaseEntity {
    private int toothNumber;
    private ToothCondition toothCondition;
    private String notes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dental_record_id")
    private DentalRecord dentalRecord;
}
