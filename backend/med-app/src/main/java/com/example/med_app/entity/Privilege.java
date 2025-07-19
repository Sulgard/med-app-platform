package com.example.med_app.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Entity
@Table(name = "privileges")
public class Privilege extends BaseEntityAudit implements GrantedAuthority {
    String name;

    @ManyToMany
    private Collection<Role> roles;

    @Override
    public String getAuthority() {
        return name;
    }
}
