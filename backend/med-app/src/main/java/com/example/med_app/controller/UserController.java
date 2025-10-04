package com.example.med_app.controller;

import com.example.med_app.dto.request.ChangeEmailRequestDTO;
import com.example.med_app.dto.request.ChangePasswordRequestDTO;
import com.example.med_app.dto.request.CreateUserRequestDTO;
import com.example.med_app.dto.request.PasswordResetRequestDTO;
import com.example.med_app.dto.response.CreateUserResponseDTO;
import com.example.med_app.dto.response.DeleteUserResponseDTO;
import com.example.med_app.dto.response.GenericResponse;
import com.example.med_app.dto.response.UserResponseDTO;
import com.example.med_app.entity.User;
import com.example.med_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/reset-password")
    public ResponseEntity<GenericResponse> resetPassword(PasswordResetRequestDTO request) {
        userService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok(new GenericResponse("Password reset successful"));
    }

    @PostMapping("/change/password")
    public ResponseEntity<GenericResponse> changeUserPassword(ChangePasswordRequestDTO request, @AuthenticationPrincipal User user) {
        userService.changeUserPassword(user, request);
        return ResponseEntity.ok(new GenericResponse("Password changed successfully"));
    }

    @PostMapping("/change/email")
    public ResponseEntity<GenericResponse> changeUserEmail(ChangeEmailRequestDTO request, @AuthenticationPrincipal User user) {
        userService.changeUserEmail(user, request);
        return ResponseEntity.ok(new GenericResponse("Email changed successfully"));
    }

    @GetMapping("user/{id}/details")
    public ResponseEntity<UserResponseDTO> getUserProfile(@PathVariable Long id) {
        UserResponseDTO response = userService.getUserDetails(id);
        return ResponseEntity.ok(response);
    }
}
