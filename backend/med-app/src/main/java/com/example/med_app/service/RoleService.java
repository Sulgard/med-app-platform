package com.example.med_app.service;

import com.example.med_app.dto.response.RoleResponseDTO;
import com.example.med_app.entity.Privilege;
import com.example.med_app.entity.Role;
import com.example.med_app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleResponseDTO getRole(Long roleId) {
        Role role  = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        Set<String> privileges = role.getPrivileges().stream()
                .map(Privilege::getAuthority)
                .collect(Collectors.toSet());

        return new RoleResponseDTO(role.getName(), privileges);
    }

    public RoleResponseDTO getRoleByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        Set<String> privileges = role.getPrivileges().stream()
                .map(Privilege::getAuthority)
                .collect(Collectors.toSet());

        return new RoleResponseDTO(role.getName(), privileges);
    }
}
