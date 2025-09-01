package com.example.med_app.service;

import com.example.med_app.dto.request.CreateUserRequestDTO;
import com.example.med_app.dto.response.CreateUserResponseDTO;
import com.example.med_app.dto.response.DeleteUserResponseDTO;
import com.example.med_app.entity.PasswordResetToken;
import com.example.med_app.entity.Role;
import com.example.med_app.entity.User;
import com.example.med_app.exceptions.TokenExpiredException;
import com.example.med_app.exceptions.TokenNotFoundException;
import com.example.med_app.repository.PasswordResetTokenRepository;
import com.example.med_app.repository.RoleRepository;
import com.example.med_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenService passwordResetTokenService;


    public CreateUserResponseDTO createUser(CreateUserRequestDTO request) {
        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new IllegalArgumentException("Role with a given name doesn't exist."));

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(role);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setGender(request.gender());
        user.setMedicalLicense(request.medicalLicense());
        user.setDateOfBirth(request.dateOfBirth());
        user.setInsurance(request.insurance());
        User savedUser = userRepository.save(user);
        return new  CreateUserResponseDTO("User created successfully");
    }

    public DeleteUserResponseDTO deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " doesn't exist"));

        user.setDeleted(true);
        userRepository.save(user);
        return new DeleteUserResponseDTO("User deleted successfully");
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " doesn't exist"));
    }

    public User getUserByPasswordResetToken(PasswordResetToken token) {
        return userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new TokenNotFoundException("User with token " + token.getToken() + " doesn't exist"));
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.validatePasswordResetToken(token);
        User user = passwordResetToken.getUser();
        changeUserPassword(user, newPassword);
        passwordResetTokenRepository.delete(passwordResetToken);
    }
}
