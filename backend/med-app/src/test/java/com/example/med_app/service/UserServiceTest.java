package com.example.med_app.service;

import com.example.med_app.dto.request.CreateUserRequestDTO;
import com.example.med_app.dto.response.CreateUserResponseDTO;
import com.example.med_app.dto.response.UserResponseDTO;
import com.example.med_app.entity.Role;
import com.example.med_app.entity.User;
import com.example.med_app.enums.Gender;
import com.example.med_app.enums.RoleName;
import com.example.med_app.repository.RoleRepository;
import com.example.med_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("Marvin");
        user.setLastName("Branagh");
        user.setPassword("1998");
        user.setEmail("marvin@test.com");
        user.setGender(Gender.MALE);
        user.setInsurance("SDFIOJESFEF");
        user.setDateOfBirth(LocalDate.of(1970, 12, 1));
        user.setPhoneNumber("1234567890");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserDetails(userId);

        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals("Marvin", response.firstName());
        assertEquals("Branagh", response.lastName());
        assertEquals("marvin@test.com", response.email());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldCreateUserWithPatientRole() {
        CreateUserRequestDTO request = new CreateUserRequestDTO(
                "marvin@test.com",
                "Marvin",
                "Branagh",
                "123478435WFSDF!",
                "+48123456789",
                Gender.MALE,
                LocalDate.of(1970,12,1),
                "",
                "SDIEFSLSE",
                "PATIENT"
        );

        Role patientRole = new Role();
        patientRole.setId(2L);
        patientRole.setName(RoleName.PATIENT.name());

        when(roleRepository.findByName("PATIENT")).thenReturn(Optional.of(patientRole));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User dummy = invocation.getArgument(0);
            dummy.setId(42L);
            return dummy;
        });

        CreateUserResponseDTO response = userService.createUser(request);
        assertNotNull(response);
        assertEquals("User created successfully", response.response());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());
        User savedUser = captor.getValue();

        assertEquals("PATIENT", savedUser.getRole().getName());
        assertEquals(Gender.MALE, savedUser.getGender());
        assertEquals("Marvin",  savedUser.getFirstName());
        assertEquals("Branagh", savedUser.getLastName());
        assertEquals("marvin@test.com", savedUser.getEmail());

        assertNotNull(savedUser.getRole());
        assertEquals(savedUser.getRole().getName(), RoleName.PATIENT.name());

        verify(roleRepository, times(1)).findByName("PATIENT");
    }
}