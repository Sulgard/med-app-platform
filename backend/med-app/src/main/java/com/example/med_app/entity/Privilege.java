package com.example.med_app.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "privileges", schema = "dental_clinic")
public class Privilege extends BaseEntityAudit implements GrantedAuthority {
    String name;

    @ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    @Override
    public String getAuthority() {
        return name;
    }
}
