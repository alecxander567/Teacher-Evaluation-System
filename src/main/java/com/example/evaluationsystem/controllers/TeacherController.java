package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.TeacherDTO;
import com.example.evaluationsystem.dto.TeacherRequestDTO;
import com.example.evaluationsystem.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Configure based on your frontend URL
public class TeacherController {

    private final TeacherService teacherService;

    /**
     * Create a new teacher
     * POST /api/teachers
     */
    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@Valid @RequestBody TeacherRequestDTO requestDTO) {
        TeacherDTO createdTeacher = teacherService.createTeacher(requestDTO);
        return new ResponseEntity<>(createdTeacher, HttpStatus.CREATED);
    }

    /**
     * Get all teachers
     * GET /api/teachers
     */
    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    /**
     * Get teacher by ID
     * GET /api/teachers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
        TeacherDTO teacher = teacherService.getTeacherById(id);
        return ResponseEntity.ok(teacher);
    }

    /**
     * Get teachers by department ID
     * GET /api/teachers/department/{departmentId}
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<TeacherDTO>> getTeachersByDepartment(@PathVariable Integer departmentId) {
        List<TeacherDTO> teachers = teacherService.getTeachersByDepartment(departmentId);
        return ResponseEntity.ok(teachers);
    }

    /**
     * Update teacher
     * PUT /api/teachers/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(
            @PathVariable Long id,
            @Valid @RequestBody TeacherRequestDTO requestDTO) {
        TeacherDTO updatedTeacher = teacherService.updateTeacher(id, requestDTO);
        return ResponseEntity.ok(updatedTeacher);
    }

    /**
     * Delete teacher
     * DELETE /api/teachers/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search teachers by name
     * GET /api/teachers/search?term={searchTerm}
     */
    @GetMapping("/search")
    public ResponseEntity<List<TeacherDTO>> searchTeachers(@RequestParam String term) {
        List<TeacherDTO> teachers = teacherService.searchTeachers(term);
        return ResponseEntity.ok(teachers);
    }

    /**
     * Get teacher count by department
     * GET /api/teachers/count/department/{departmentId}
     */
    @GetMapping("/count/department/{departmentId}")
    public ResponseEntity<Long> getTeacherCountByDepartment(@PathVariable Integer departmentId) {
        long count = teacherService.getTeacherCountByDepartment(departmentId);
        return ResponseEntity.ok(count);
    }

    /**
     * Check if teacher exists
     * GET /api/teachers/{id}/exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsTeacher(@PathVariable Long id) {
        boolean exists = teacherService.existsTeacher(id);
        return ResponseEntity.ok(exists);
    }
}