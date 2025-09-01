package com.example.med_app.repository;

import com.example.med_app.entity.PasswordResetToken;
import com.example.med_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByPasswordResetToken(PasswordResetToken token);
}
