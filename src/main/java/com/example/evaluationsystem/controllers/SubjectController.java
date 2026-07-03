// src/main/java/com/example/evaluationsystem/controllers/SubjectController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.SubjectDTO;
import com.example.evaluationsystem.dto.SubjectDetailDTO;
import com.example.evaluationsystem.dto.SubjectRequestDTO;
import com.example.evaluationsystem.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubjectController {

    private final SubjectService subjectService;

    /**
     * Create a new subject
     * POST /api/subjects
     */
    @PostMapping
    public ResponseEntity<SubjectDTO> createSubject(@Valid @RequestBody SubjectRequestDTO requestDTO) {
        SubjectDTO createdSubject = subjectService.createSubject(requestDTO);
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    /**
     * Get all subjects
     * GET /api/subjects
     */
    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    /**
     * Get subject by ID
     * GET /api/subjects/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        SubjectDTO subject = subjectService.getSubjectById(id);
        return ResponseEntity.ok(subject);
    }

    /**
     * Get subject details with department info
     * GET /api/subjects/{id}/details
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<SubjectDetailDTO> getSubjectDetailById(@PathVariable Long id) {
        SubjectDetailDTO subjectDetail = subjectService.getSubjectDetailById(id);
        return ResponseEntity.ok(subjectDetail);
    }

    /**
     * Get subjects by department
     * GET /api/subjects/department/{departmentId}
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<SubjectDTO>> getSubjectsByDepartment(@PathVariable Integer departmentId) {
        List<SubjectDTO> subjects = subjectService.getSubjectsByDepartment(departmentId);
        return ResponseEntity.ok(subjects);
    }

    /**
     * Update subject
     * PUT /api/subjects/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody SubjectRequestDTO requestDTO) {
        SubjectDTO updatedSubject = subjectService.updateSubject(id, requestDTO);
        return ResponseEntity.ok(updatedSubject);
    }

    /**
     * Delete subject
     * DELETE /api/subjects/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search subjects by code or name
     * GET /api/subjects/search?term={searchTerm}
     */
    @GetMapping("/search")
    public ResponseEntity<List<SubjectDTO>> searchSubjects(@RequestParam String term) {
        List<SubjectDTO> subjects = subjectService.searchSubjects(term);
        return ResponseEntity.ok(subjects);
    }

    /**
     * Get subject count by department
     * GET /api/subjects/count/department/{departmentId}
     */
    @GetMapping("/count/department/{departmentId}")
    public ResponseEntity<Long> getSubjectCountByDepartment(@PathVariable Integer departmentId) {
        long count = subjectService.getSubjectCountByDepartment(departmentId);
        return ResponseEntity.ok(count);
    }

    /**
     * Check if subject exists
     * GET /api/subjects/{id}/exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsSubject(@PathVariable Long id) {
        boolean exists = subjectService.existsSubject(id);
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if subject code exists
     * GET /api/subjects/exists?code={subjectCode}
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsSubjectByCode(@RequestParam String code) {
        boolean exists = subjectService.existsSubjectByCode(code);
        return ResponseEntity.ok(exists);
    }
}