package com.example.med_app.security.jwt;

import com.example.med_app.entity.RefreshToken;
import com.example.med_app.entity.User;
import com.example.med_app.exceptions.TokenExpiredException;
import com.example.med_app.exceptions.TokenNotFoundException;
import com.example.med_app.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final long REFRESH_TOKEN_EXPIRY = 604800000; //7days

    public RefreshToken createRefreshToken(UserDetails userDetails) {
        String token = jwtService.generateRefreshToken(userDetails, REFRESH_TOKEN_EXPIRY);
        User user = (User) userDetails;
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRY));
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteByToken(String refreshToken) {
        RefreshToken refreshTokenOB = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException("Refresh token not found"));
        refreshTokenRepository.delete(refreshTokenOB);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if(refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenExpiredException("Refresh token expired");
        }
        return refreshToken;
    }
}
