package com.example.med_app.service;

import com.example.med_app.dto.request.ChangeEmailRequestDTO;
import com.example.med_app.dto.request.ChangePasswordRequestDTO;
import com.example.med_app.dto.request.CreateUserRequestDTO;
import com.example.med_app.dto.response.CreateUserResponseDTO;
import com.example.med_app.dto.response.DeleteUserResponseDTO;
import com.example.med_app.dto.response.UserResponseDTO;
import com.example.med_app.entity.PasswordResetToken;
import com.example.med_app.entity.Role;
import com.example.med_app.entity.User;
import com.example.med_app.exceptions.TokenNotFoundException;
import com.example.med_app.repository.PasswordResetTokenRepository;
import com.example.med_app.repository.RoleRepository;
import com.example.med_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        userRepository.save(user);
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

    public void saveNewPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void changeUserEmail(User user, ChangeEmailRequestDTO request) {
        if (request.password() == null || request.password().isBlank() && request.repeatPassword() == null || request.repeatPassword().isBlank()) {
            throw new IllegalArgumentException("Password is empty");
        }
        if (! passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Passwords doesn't match");
        }
        if (!request.password().equals(request.repeatPassword())) {
            throw new IllegalArgumentException("Passwords doesn't match");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email is in use");
        }

        user.setEmail(request.email());
        userRepository.save(user);
    }

    public void changeUserPassword(User user, ChangePasswordRequestDTO request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String oldPassword = request.oldPassword();
        String newPassword = request.newPassword();
        String newPassword2 = request.repeatNewPassword();

        String userPassword = user.getPassword();

        if (!passwordEncoder.matches(oldPassword, userPassword)) {
            throw new IllegalArgumentException("Old password doesn't match");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password is empty");
        }

        if (newPassword2 == null || newPassword2.isBlank()) {
            throw new IllegalArgumentException("Password is empty");
        }

        if(newPassword.equals(oldPassword)) {
            throw new IllegalArgumentException("New password matches old password");
        }

        if(!newPassword.equals(newPassword2)) {
            throw new IllegalArgumentException("Passwords don't match");
        }

        saveNewPassword(user, newPassword);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.validatePasswordResetToken(token);
        User user = passwordResetToken.getUser();
        saveNewPassword(user, newPassword);
        passwordResetTokenRepository.delete(passwordResetToken);
    }

    @Cacheable(value = "USER_CACHE", key = "#userId")
    public UserResponseDTO getUserDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " doesn't exist"));

        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender().toString(),
                user.getDateOfBirth().toString(),
                user.getInsurance(),
                user.getPhoneNumber(),
                user.getEmail()
        );
    }
}
