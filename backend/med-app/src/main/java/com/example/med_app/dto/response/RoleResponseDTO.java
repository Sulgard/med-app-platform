package com.example.med_app.dto.response;

import java.util.Set;

public record RoleResponseDTO(
        String name,
        Set<String> privilages
) {
}
