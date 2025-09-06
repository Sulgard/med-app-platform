package com.example.med_app.dto.request;

public record ChangeEmailRequestDTO(
        String email,
        String password,
        String repeatPassword
) {
}
