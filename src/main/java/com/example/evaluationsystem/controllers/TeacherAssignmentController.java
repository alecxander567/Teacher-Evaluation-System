// TeacherAssignmentController.java
package com.example.evaluationsystem.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.evaluationsystem.dto.TeacherAssignmentDTO;
import com.example.evaluationsystem.dto.TeacherSelectionDTO;
import com.example.evaluationsystem.service.TeacherAssignmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teacher-assignments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TeacherAssignmentController {

    private final TeacherAssignmentService teacherAssignmentService;

    @GetMapping
    public ResponseEntity<List<TeacherAssignmentDTO>> getAllAssignments() {
        return ResponseEntity.ok(teacherAssignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherAssignmentDTO> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherAssignmentService.getAssignmentById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeacherAssignmentDTO>> getAssignmentsByAcademicYearAndSemester(
            @RequestParam String academicYear,
            @RequestParam String semester) {
        return ResponseEntity.ok(
                teacherAssignmentService.getAssignmentsByAcademicYearAndSemester(academicYear, semester));
    }

    @GetMapping("/selection")
    public ResponseEntity<List<TeacherSelectionDTO>> getTeacherSelectionList(
            @RequestParam String academicYear,
            @RequestParam String semester,
            @RequestParam(required = false) Integer departmentId) {
        return ResponseEntity.ok(
                teacherAssignmentService.getTeacherSelectionList(academicYear, semester, departmentId));
    }

    @PostMapping
    public ResponseEntity<TeacherAssignmentDTO> createAssignment(
            @Valid @RequestBody TeacherAssignmentDTO request) {
        TeacherAssignmentDTO created = teacherAssignmentService.createAssignment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}