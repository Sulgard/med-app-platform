package com.example.med_app.service;

import com.example.med_app.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class MailService {
    private final MessageSource messages;

    private SimpleMailMessage constructEmail(String subject,
                                             String body,
                                             User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom("dentalclinic@test.com");
        return email;
    }

    public SimpleMailMessage constructResetTokenEmail(
            String contextPath,
            String token,
            User user) {
        String url = contextPath + "/user/change-password?token=" + token;
        String message = "Click the link below to reset your password";
        return constructEmail("Reset Password", message + " \r \n" + url, user);
    }
}
