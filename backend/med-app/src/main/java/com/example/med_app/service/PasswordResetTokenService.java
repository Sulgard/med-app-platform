package com.example.med_app.service;

import com.example.med_app.entity.PasswordResetToken;
import com.example.med_app.entity.User;
import com.example.med_app.exceptions.TokenExpiredException;
import com.example.med_app.exceptions.TokenNotFoundException;
import com.example.med_app.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private static final int EXPIRATION = 60 * 24;


    public void createPasswordResetToken(String token, User user) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(Instant.now().plus(EXPIRATION, ChronoUnit.MINUTES));
        passwordResetTokenRepository.save(resetToken);
    }

    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));
    }

    public PasswordResetToken validatePasswordResetToken(String token) {
        PasswordResetToken passwordToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));

        if(passwordToken.getExpiryDate().isBefore(Instant.now())) {
            throw new TokenExpiredException("Token has expired");
        }

        return passwordToken;
    }
}
