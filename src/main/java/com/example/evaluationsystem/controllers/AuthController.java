// src/main/java/com/example/evaluationsystem/controllers/AuthController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.config.JwtService;
import com.example.evaluationsystem.dto.AdminDTO;
import com.example.evaluationsystem.dto.ErrorResponse;
import com.example.evaluationsystem.dto.LoginRequest;
import com.example.evaluationsystem.dto.LoginResponse;
import com.example.evaluationsystem.dto.SignupResponse;
import com.example.evaluationsystem.model.Admin;
import com.example.evaluationsystem.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AdminDTO adminDTO) {
        if (adminService.existsByEmail(adminDTO.getEmail())) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Email already registered",
                    "An admin with this email already exists",
                    HttpStatus.CONFLICT.value()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        Admin admin = new Admin();
        admin.setFirstName(adminDTO.getFirstName());
        admin.setLastName(adminDTO.getLastName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPassword(adminDTO.getPassword());
        admin.setRole(adminDTO.getRole() != null ? adminDTO.getRole() : "admin");

        try {
            Admin createdAdmin = adminService.createAdmin(admin);

            SignupResponse response = new SignupResponse(
                    "Admin registered successfully",
                    createdAdmin.getId(),
                    createdAdmin.getEmail(),
                    createdAdmin.getFirstName(),
                    createdAdmin.getLastName(),
                    createdAdmin.getRole(),
                    createdAdmin.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Registration failed",
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            String token = jwtService.generateToken(userDetails);

            // FIXED: Using findAdminByEmail instead of getAdminByEmail
            Admin admin = adminService.findAdminByEmail(loginRequest.getEmail());

            LoginResponse response = new LoginResponse(
                    "Login successful",
                    admin.getId(),
                    admin.getEmail(),
                    admin.getFirstName(),
                    admin.getLastName(),
                    admin.getRole(),
                    token
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Login failed",
                    "Invalid email or password",
                    HttpStatus.UNAUTHORIZED.value()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}