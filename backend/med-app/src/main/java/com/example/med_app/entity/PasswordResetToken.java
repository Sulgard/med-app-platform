package com.example.med_app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;


@Data
@Entity
@Table(name = "password_reset_tokens", schema = "dental_clinic")
public class PasswordResetToken extends BaseEntityAudit {
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    @Column(name = "expiry_date")
    private Instant expiryDate;
}
