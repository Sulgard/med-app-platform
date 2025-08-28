package com.example.med_app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "password_reset_tokens", schema = "dental_clinic")
public class PasswordResetToken extends BaseEntityAudit {
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    private Date expiryDate;
}
