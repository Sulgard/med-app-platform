package com.example.med_app.controller;

import com.example.med_app.dto.request.CreateUserRequestDTO;
import com.example.med_app.dto.response.CreateUserResponseDTO;
import com.example.med_app.dto.response.DeleteUserResponseDTO;
import com.example.med_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/test/create")
    public ResponseEntity<CreateUserResponseDTO> addUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO){
        CreateUserResponseDTO response = userService.createUser(createUserRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test/delete/{id}")
    public ResponseEntity<DeleteUserResponseDTO> deleteUser(@PathVariable Long id) {
        DeleteUserResponseDTO response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
}
