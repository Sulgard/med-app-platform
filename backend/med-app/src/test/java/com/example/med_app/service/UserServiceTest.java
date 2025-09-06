package com.example.med_app.service;

import com.example.med_app.dto.request.ChangeEmailRequestDTO;
import com.example.med_app.dto.request.ChangePasswordRequestDTO;
import com.example.med_app.entity.User;
import com.example.med_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;
    //TODO: Zrobic testy

    @Test
    void changeUserEmailShouldBeSuccessful() {
        User user = new User();
        user.setEmail("testemail@test.com");
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        user.setPassword(passwordEncoder.encode("123456"));

        ChangeEmailRequestDTO request = new ChangeEmailRequestDTO(
                "testemail2@test.com",
                "123456",
                "123456"
        );

        when(passwordEncoder.matches("123456", "encodedPassword")).thenReturn(true);

        userService.changeUserEmail(user, request);
        assertEquals("testemail2@test.com", user.getEmail());
    }

    @Test
    void changeUserEmailShouldThrowEmailIsInUse() {
        User user = new User();
        user.setEmail("testemail@test.com");
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        user.setPassword(passwordEncoder.encode("123456"));

        User user2 = new User();
        user.setEmail("testemail2@test.com");
        userRepository.save(user2);

        ChangeEmailRequestDTO request = new ChangeEmailRequestDTO(
                "testemail2@test.com",
                "123456",
                "123456"
        );

        when(passwordEncoder.matches("123456", "encodedPassword")).thenReturn(true);
        when(userRepository.findByEmail("testemail2@test.com")).thenReturn(Optional.of(user2));

        assertThrows(IllegalArgumentException.class, () -> userService.changeUserEmail(user, request));
        verify(userRepository).findByEmail("testemail2@test.com");
    }

    @Test
    void changeUserPasswordShouldThrowPasswordIsEmpty() {
        User user = new User();
        user.setPassword("encodedPassword");
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO(
                "123456",
                "",
                "654321"
        );

        assertThrows(IllegalArgumentException.class, () -> userService.changeUserPassword(user, request));
        verify(userRepository, never()).save(any(User.class));
    }

}