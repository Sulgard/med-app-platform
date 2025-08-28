package com.example.med_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens", schema = "dental_clinic")
public class RefreshToken extends BaseEntityAudit {
    @Column(unique = true, nullable = false)
    private String token;
    private Instant expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
