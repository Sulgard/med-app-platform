package com.example.med_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "roles")
public class Role extends BaseEntityAudit implements GrantedAuthority {
    private String name;

    @ManyToMany
    @JoinTable(
            name = "role_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges;

    @Override
    public String getAuthority() {
        return name;
    }
}
