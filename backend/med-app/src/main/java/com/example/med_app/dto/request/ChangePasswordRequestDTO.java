package com.example.med_app.dto.request;

public record ChangePasswordRequestDTO(
        String oldPassword,
        String newPassword,
        String repeatNewPassword
) {
}
