// src/main/java/com/example/evaluationsystem/controllers/AdminController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.AdminResponse;
import com.example.evaluationsystem.dto.AdminUpdateDTO;
import com.example.evaluationsystem.dto.ErrorResponse;
import com.example.evaluationsystem.model.Admin;
import com.example.evaluationsystem.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Get current admin profile
     * GET /api/admin/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentAdminProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentAdminEmail = authentication.getName();
            
            Admin admin = adminService.findAdminByEmail(currentAdminEmail);
            AdminResponse response = convertToResponse(admin);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Failed to fetch profile",
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Update current admin profile
     * PUT /api/admin/profile
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateCurrentAdminProfile(@Valid @RequestBody AdminUpdateDTO updateDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentAdminEmail = authentication.getName();
            
            Admin currentAdmin = adminService.findAdminByEmail(currentAdminEmail);
            Admin updatedAdmin = adminService.updateAdmin(currentAdmin.getId(), updateDTO);
            
            AdminResponse response = convertToResponse(updatedAdmin);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Profile updated successfully");
            result.put("admin", response);
            
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Update failed",
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST.value()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Update failed",
                    "An unexpected error occurred: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Update any admin by ID (Super admin functionality)
     * PUT /api/admin/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdminById(
            @PathVariable Long id, 
            @Valid @RequestBody AdminUpdateDTO updateDTO) {
        try {
            Admin updatedAdmin = adminService.updateAdmin(id, updateDTO);
            AdminResponse response = convertToResponse(updatedAdmin);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Admin updated successfully");
            result.put("admin", response);
            
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Update failed",
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST.value()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Update failed",
                    "An unexpected error occurred: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Delete admin by ID
     * DELETE /api/admin/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        try {
            // Prevent admin from deleting themselves
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentAdminEmail = authentication.getName();
            Admin currentAdmin = adminService.findAdminByEmail(currentAdminEmail);
            
            if (currentAdmin.getId().equals(id)) {
                ErrorResponse errorResponse = new ErrorResponse(
                        "Deletion failed",
                        "You cannot delete your own account",
                        HttpStatus.BAD_REQUEST.value()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            adminService.deleteAdmin(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Admin deleted successfully");
            result.put("id", id);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Deletion failed",
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private AdminResponse convertToResponse(Admin admin) {
        AdminResponse response = new AdminResponse();
        response.setId(admin.getId());
        response.setFirstName(admin.getFirstName());
        response.setLastName(admin.getLastName());
        response.setEmail(admin.getEmail());
        response.setRole(admin.getRole());
        response.setCreatedAt(admin.getCreatedAt());
        response.setUpdatedAt(admin.getUpdatedAt());
        return response;
    }
}