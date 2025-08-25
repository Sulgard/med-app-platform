package com.example.med_app.controller;

import com.example.med_app.dto.response.RoleResponseDTO;
import com.example.med_app.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/id/{id}")
    public ResponseEntity<RoleResponseDTO> getRole(@PathVariable Long id) {
        RoleResponseDTO role = roleService.getRole(id);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoleResponseDTO> getRoleByName(@PathVariable String name) {
        RoleResponseDTO role = roleService.getRoleByName(name);
        return ResponseEntity.ok(role);
    }
}
