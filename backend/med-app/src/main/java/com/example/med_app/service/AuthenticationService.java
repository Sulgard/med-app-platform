package com.example.med_app.service;

import com.example.med_app.dto.request.AuthRequestDTO;
import com.example.med_app.dto.request.CreateUserRequestDTO;
import com.example.med_app.dto.response.JWTresponseDTO;
import com.example.med_app.dto.response.UserIdResponseDTO;
import com.example.med_app.entity.RefreshToken;
import com.example.med_app.entity.Role;
import com.example.med_app.entity.User;
import com.example.med_app.repository.RoleRepository;
import com.example.med_app.repository.UserRepository;
import com.example.med_app.security.jwt.JwtService;
import com.example.med_app.security.jwt.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public UserIdResponseDTO register(CreateUserRequestDTO request) {
        Role role = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setRole(role);
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setDateOfBirth(request.dateOfBirth());
        user.setMedicalLicense(request.medicalLicense());
        user.setInsurance(request.insurance());
        user.setGender(request.gender());
        userRepository.save(user);
        return new UserIdResponseDTO(user.getId());
    }

    public JWTresponseDTO login(AuthRequestDTO request) {
        try  {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        UserDetails user = userDetailsService.loadUserByUsername(request.email());
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new JWTresponseDTO(accessToken, refreshToken.getToken());
    }

    //TODO: forgot password/reset password, change password and change email
}
