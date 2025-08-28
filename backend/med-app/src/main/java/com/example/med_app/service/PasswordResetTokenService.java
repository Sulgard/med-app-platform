package com.example.med_app.service;

import com.example.med_app.entity.PasswordResetToken;
import com.example.med_app.entity.User;
import com.example.med_app.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserService userService;
    private final MessageSource messages;
    private static final int EXPIRATION = 60 * 24;


    public void createPasswordResetToken(String token, User user) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(new Date(System.currentTimeMillis() + EXPIRATION * 60 * 1000L));
        passwordResetTokenRepository.save(resetToken);
    }

    private SimpleMailMessage constructEmail(String subject,
                                             String body,
                                             User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        //env.getProperty("support.email")
        email.setFrom(email.getFrom());
        return email;
    }

    private SimpleMailMessage constructResetTokenEmail(
        String contextPath,
        Locale locale,
        String token,
        User user) {
        String url = contextPath + "/user/changePassword?token=" + token;
        String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r \n" + url, user);
    }
}
