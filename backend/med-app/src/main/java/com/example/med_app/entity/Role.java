package com.example.med_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "roles", schema = "dental_clinic")
public class Role extends BaseEntityAudit implements GrantedAuthority {
    private String name;

    @ManyToMany
    @JoinTable(
            name = "role_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges = new HashSet<>();

    @Override
    public String getAuthority() {
        return name;
    }
}
