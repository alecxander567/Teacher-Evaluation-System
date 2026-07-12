// src/main/java/com/example/evaluationsystem/service/AdminService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.AdminUpdateDTO;
import com.example.evaluationsystem.model.Admin;
import com.example.evaluationsystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Admin createAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public Admin authenticateAdmin(String email, String rawPassword) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return admin;
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Admin findAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));
    }

    @Transactional
    public Admin updateAdmin(Long id, AdminUpdateDTO updateDTO) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));

        // Check if email is being changed and if it's already taken
        if (updateDTO.getEmail() != null && !admin.getEmail().equals(updateDTO.getEmail())) {
            if (adminRepository.existsByEmail(updateDTO.getEmail())) {
                throw new RuntimeException("Email already in use by another admin");
            }
            admin.setEmail(updateDTO.getEmail());
        }

        // Update fields
        if (updateDTO.getFirstName() != null) {
            admin.setFirstName(updateDTO.getFirstName());
        }
        
        if (updateDTO.getLastName() != null) {
            admin.setLastName(updateDTO.getLastName());
        }

        // Only update password if provided
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        return adminRepository.save(admin);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new RuntimeException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }
}