package com.example.med_app.dto.response;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String gender,
        String dateOfBirth,
        String insuranceNumber,
        String phoneNumber,
        String email
) {
}
