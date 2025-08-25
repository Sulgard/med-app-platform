package com.example.med_app.service;

import com.example.med_app.dto.request.CreateUserRequestDTO;
import com.example.med_app.dto.response.CreateUserResponseDTO;
import com.example.med_app.dto.response.DeleteUserResponseDTO;
import com.example.med_app.entity.Role;
import com.example.med_app.entity.User;
import com.example.med_app.repository.RoleRepository;
import com.example.med_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


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



}
