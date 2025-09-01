package com.example.med_app.dto.request;

import jakarta.validation.constraints.Pattern;

public record PasswordResetRequestDTO(
        String oldPassword,
        String token,

        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
        )
        String newPassword
) {
}
