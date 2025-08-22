package com.example.med_app.dto.request;

import com.example.med_app.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CreateUserRequestDTO(
        @NotBlank String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
            @Pattern(
            regexp = "^\\+[1-9]\\d{1,14}$",
            message = "The telephone number must match with the standard E.164 (e.g. +48123456789)"
        )
        @NotBlank String password,
        @NotBlank String phoneNumber,
        @NotBlank Gender gender,
        @NotBlank LocalDate dateOfBirth,
        String medicalLicense,
        @NotBlank String insurance,
        @NotBlank String role
) {
}
