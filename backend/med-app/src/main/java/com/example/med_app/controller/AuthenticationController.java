package com.example.med_app.controller;

import com.example.med_app.dto.request.AuthRequestDTO;
import com.example.med_app.dto.request.CreateUserRequestDTO;
import com.example.med_app.dto.request.RefreshTokenRequestDTO;
import com.example.med_app.dto.response.GenericResponse;
import com.example.med_app.dto.response.JWTresponseDTO;
import com.example.med_app.dto.response.UserIdResponseDTO;
import com.example.med_app.entity.PasswordResetToken;
import com.example.med_app.entity.RefreshToken;
import com.example.med_app.entity.User;
import com.example.med_app.exceptions.InvalidTokenException;
import com.example.med_app.repository.PasswordResetTokenRepository;
import com.example.med_app.security.jwt.JwtService;
import com.example.med_app.security.jwt.RefreshTokenService;
import com.example.med_app.service.AuthenticationService;
import com.example.med_app.service.MailService;
import com.example.med_app.service.PasswordResetTokenService;
import com.example.med_app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.MessageSource;


import java.util.UUID;

import static com.example.med_app.util.UrlUtils.getAppUrl;


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
    private final MailSender mailSender;
    private final MailService mailService;
    private final MessageSource messages;


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

    @PostMapping("/reset-password")
    public GenericResponse resetPassword(HttpServletRequest request, @RequestParam("email") String email) {
        User user = userService.findUserByEmail(email);
        String token = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetToken(token, user);
        mailSender.send(mailService.constructResetTokenEmail(getAppUrl(request),token, user));
        return new GenericResponse("Reset link has been sent successfully!");
    }

    @GetMapping("/validate-password-reset-token")
    public ResponseEntity<GenericResponse> showChangePasswordPage(@RequestParam("token") String token) {
        PasswordResetToken validateToken = passwordResetTokenService.validatePasswordResetToken(token);
        if(validateToken == null) {
            return ResponseEntity.badRequest().body(new GenericResponse("Invalid token"));
        }
        return ResponseEntity.ok(new GenericResponse("Valid token"));
    }
}
