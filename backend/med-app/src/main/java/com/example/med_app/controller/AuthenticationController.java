package com.example.med_app.controller;

import com.example.med_app.dto.request.AuthRequestDTO;
import com.example.med_app.dto.request.CreateUserRequestDTO;
import com.example.med_app.dto.request.RefreshTokenDTO;
import com.example.med_app.dto.request.RefreshTokenRequestDTO;
import com.example.med_app.dto.response.JWTresponseDTO;
import com.example.med_app.dto.response.JwtResponseDTO;
import com.example.med_app.dto.response.UserIdDTO;
import com.example.med_app.dto.response.UserIdResponseDTO;
import com.example.med_app.entity.RefreshToken;
import com.example.med_app.exceptions.InvalidTokenException;
import com.example.med_app.security.jwt.JwtService;
import com.example.med_app.security.jwt.RefreshTokenService;
import com.example.med_app.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public UserIdResponseDTO register(@RequestBody CreateUserRequestDTO request) {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTresponseDTO> login(@RequestBody AuthRequestDTO request) {
        JWTresponseDTO response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody String request) {
        return refreshTokenService.findByToken(request)
                .map(refreshTokenService::verifyExpiration)
                .map(token -> {
                    refreshTokenService.deleteByToken(token.getToken());

                    RefreshToken newRefreshToken = refreshTokenService
                            .createRefreshToken(token.getUser());

                    String accessToken = jwtService.generateAccessToken(
                            token.getUser());

                    return ResponseEntity.ok(new JWTresponseDTO(
                            accessToken,
                            newRefreshToken.getToken()
                    ));
                })
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequestDTO request) {
        refreshTokenService.deleteByToken(request.refreshToken());
        return ResponseEntity.ok("Logout successful");
    }
}
