package com.example.med_app.dto.response;

public record JWTresponseDTO(
        String accessToken,
        String refreshToken) {
}
