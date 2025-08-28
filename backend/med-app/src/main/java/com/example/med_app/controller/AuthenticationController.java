package com.example.med_app.controller;

import com.example.med_app.dto.request.AuthRequestDTO;
import com.example.med_app.dto.request.CreateUserRequestDTO;
import com.example.med_app.dto.request.RefreshTokenRequestDTO;
import com.example.med_app.dto.response.GenericResponse;
import com.example.med_app.dto.response.JWTresponseDTO;
import com.example.med_app.dto.response.UserIdResponseDTO;
import com.example.med_app.entity.RefreshToken;
import com.example.med_app.entity.User;
import com.example.med_app.exceptions.InvalidTokenException;
import com.example.med_app.exceptions.UserNotFoundException;
import com.example.med_app.repository.PasswordResetTokenRepository;
import com.example.med_app.security.jwt.JwtService;
import com.example.med_app.security.jwt.RefreshTokenService;
import com.example.med_app.service.AuthenticationService;
import com.example.med_app.service.PasswordResetTokenService;
import com.example.med_app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordResetTokenService passwordResetTokenService;

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
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        return refreshTokenService.findByToken(request.refreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(token -> {
                    User userRef = token.getUser();

                    refreshTokenService.deleteByToken(token.getToken());

                    RefreshToken newRefreshToken = refreshTokenService
                            .createRefreshToken(userRef);

                    String accessToken = jwtService.generateAccessToken(userRef);

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

//    @PostMapping("/resetPassword")
//    public GenericResponse resetPassword(HttpServletRequest request, @RequestParam("email") String email) {
//        User user = userService.findUserByEmail(email);
//        if (user == null) {
//            throw new UserNotFoundException("User with given email doesn't exist");
//        }
//        String token = UUID.randomUUID().toString();
//        passwordResetTokenService.createPasswordResetToken(token, user);
//    }

}
