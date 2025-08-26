package com.example.med_app.dto.request;

import com.example.med_app.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


public record CreateUserRequestDTO(
        @NotBlank String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
        )
        @NotBlank String password,
        @Pattern(
                regexp = "^\\+[1-9]\\d{1,14}$",
                message = "The telephone number must match with the standard E.164 (e.g. +48123456789)"
        )
        @NotBlank String phoneNumber,
        @NotBlank Gender gender,
        @NotBlank LocalDate dateOfBirth,
        String medicalLicense,
        @NotBlank String insurance,
        @NotBlank String role
) {
}
