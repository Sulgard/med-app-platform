package com.example.med_app.entity;

import com.example.med_app.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Data
@Entity
@Table(name = "users", schema = "dental_clinic")
public class User extends BaseEntityAudit implements UserDetails {

    private String email;
    private String firstName;
    private String lastName;

    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate dateOfBirth;
    private String medicalLicense;
    private String insurance;
    private boolean isDeleted;
    private Instant lastLogin;
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Prescription> prescriptions = new ArrayList<>();
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<DentalRecord> dentalRecords = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (role != null) {
            authorities.add(role);
            authorities.addAll(role.getPrivileges());
        }
        return authorities;
    }

}
