// src/main/java/com/example/evaluationsystem/controllers/DepartmentController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.DepartmentDTO;
import com.example.evaluationsystem.dto.DepartmentDetailDTO;
import com.example.evaluationsystem.dto.DepartmentRequestDTO;
import com.example.evaluationsystem.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * Create a new department
     * POST /api/departments
     */
    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO requestDTO) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(requestDTO);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    /**
     * Get all departments
     * GET /api/departments
     */
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    /**
     * Get department by ID
     * GET /api/departments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Integer id) {
        DepartmentDTO department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }

    /**
     * Get department details with teachers
     * GET /api/departments/{id}/details
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<DepartmentDetailDTO> getDepartmentDetailById(@PathVariable Integer id) {
        DepartmentDetailDTO departmentDetail = departmentService.getDepartmentDetailById(id);
        return ResponseEntity.ok(departmentDetail);
    }

    /**
     * Update department
     * PUT /api/departments/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @PathVariable Integer id,
            @Valid @RequestBody DepartmentRequestDTO requestDTO) {
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(id, requestDTO);
        return ResponseEntity.ok(updatedDepartment);
    }

    /**
     * Delete department
     * DELETE /api/departments/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search departments by name
     * GET /api/departments/search?term={searchTerm}
     */
    @GetMapping("/search")
    public ResponseEntity<List<DepartmentDTO>> searchDepartments(@RequestParam String term) {
        List<DepartmentDTO> departments = departmentService.searchDepartments(term);
        return ResponseEntity.ok(departments);
    }

    /**
     * Get departments with teachers
     * GET /api/departments/with-teachers
     */
    @GetMapping("/with-teachers")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentsWithTeachers() {
        List<DepartmentDTO> departments = departmentService.getDepartmentsWithTeachers();
        return ResponseEntity.ok(departments);
    }

    /**
     * Get departments without teachers
     * GET /api/departments/without-teachers
     */
    @GetMapping("/without-teachers")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentsWithoutTeachers() {
        List<DepartmentDTO> departments = departmentService.getDepartmentsWithoutTeachers();
        return ResponseEntity.ok(departments);
    }

    /**
     * Get teacher count for a department
     * GET /api/departments/{id}/teacher-count
     */
    @GetMapping("/{id}/teacher-count")
    public ResponseEntity<Long> getTeacherCountByDepartment(@PathVariable Integer id) {
        long count = departmentService.getTeacherCountByDepartment(id);
        return ResponseEntity.ok(count);
    }

    /**
     * Check if department exists
     * GET /api/departments/{id}/exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsDepartment(@PathVariable Integer id) {
        boolean exists = departmentService.existsDepartment(id);
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if department exists by name
     * GET /api/departments/exists?name={name}
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsDepartmentByName(@RequestParam String name) {
        boolean exists = departmentService.existsDepartmentByName(name);
        return ResponseEntity.ok(exists);
    }
}