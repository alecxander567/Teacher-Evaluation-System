// src/main/java/com/example/evaluationsystem/controllers/TeacherSubjectController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.TeacherSubjectDTO;
import com.example.evaluationsystem.dto.TeacherSubjectDetailDTO;
import com.example.evaluationsystem.dto.TeacherSubjectRequestDTO;
import com.example.evaluationsystem.service.TeacherSubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TeacherSubjectController {

    private final TeacherSubjectService teacherSubjectService;

    /**
     * Assign a teacher to a subject
     * POST /api/assignments
     */
    @PostMapping
    public ResponseEntity<TeacherSubjectDTO> assignTeacherToSubject(
            @Valid @RequestBody TeacherSubjectRequestDTO requestDTO) {
        TeacherSubjectDTO assignment = teacherSubjectService.assignTeacherToSubject(requestDTO);
        return new ResponseEntity<>(assignment, HttpStatus.CREATED);
    }

    /**
     * Get all assignments or filter by parameters
     * GET /api/assignments
     */
    @GetMapping
    public ResponseEntity<List<TeacherSubjectDTO>> getAllAssignments(
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) String semester) {
        
        List<TeacherSubjectDTO> assignments;
        
        if (teacherId != null && academicYear != null && semester != null) {
            assignments = teacherSubjectService.getAssignmentsByTeacherAndSemester(teacherId, academicYear, semester);
        } else if (subjectId != null && academicYear != null && semester != null) {
            assignments = teacherSubjectService.getAssignmentsBySubjectAndSemester(subjectId, academicYear, semester);
        } else if (teacherId != null) {
            assignments = teacherSubjectService.getAssignmentsByTeacher(teacherId);
        } else if (subjectId != null) {
            assignments = teacherSubjectService.getAssignmentsBySubject(subjectId);
        } else if (academicYear != null && semester != null) {
            assignments = teacherSubjectService.getAssignmentsByAcademicYearAndSemester(academicYear, semester);
        } else {
            // FIX: Return all assignments when no filters are provided
            assignments = teacherSubjectService.getAllAssignments();
        }
        
        return ResponseEntity.ok(assignments);
    }

    /**
     * Get assignment by ID
     * GET /api/assignments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeacherSubjectDTO> getAssignmentById(@PathVariable Long id) {
        TeacherSubjectDTO assignment = teacherSubjectService.getAssignmentById(id);
        return ResponseEntity.ok(assignment);
    }

    /**
     * Get assignment details by ID
     * GET /api/assignments/{id}/details
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<TeacherSubjectDetailDTO> getAssignmentDetailById(@PathVariable Long id) {
        TeacherSubjectDetailDTO assignmentDetail = teacherSubjectService.getAssignmentDetailById(id);
        return ResponseEntity.ok(assignmentDetail);
    }

    /**
     * Update assignment
     * PUT /api/assignments/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeacherSubjectDTO> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody TeacherSubjectRequestDTO requestDTO) {
        TeacherSubjectDTO updatedAssignment = teacherSubjectService.updateAssignment(id, requestDTO);
        return ResponseEntity.ok(updatedAssignment);
    }

    /**
     * Remove assignment
     * DELETE /api/assignments/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAssignment(@PathVariable Long id) {
        teacherSubjectService.removeAssignment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if assignment exists
     * GET /api/assignments/{id}/exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsAssignment(@PathVariable Long id) {
        boolean exists = teacherSubjectService.existsAssignment(id);
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if teacher is assigned to subject
     * GET /api/assignments/check?teacherId={teacherId}&subjectId={subjectId}&academicYear={academicYear}&semester={semester}
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> isTeacherAssignedToSubject(
            @RequestParam Long teacherId,
            @RequestParam Long subjectId,
            @RequestParam String academicYear,
            @RequestParam String semester) {
        boolean assigned = teacherSubjectService.isTeacherAssignedToSubject(
            teacherId, subjectId, academicYear, semester
        );
        return ResponseEntity.ok(assigned);
    }

    /**
     * Get assignment count by teacher
     * GET /api/assignments/count/teacher/{teacherId}
     */
    @GetMapping("/count/teacher/{teacherId}")
    public ResponseEntity<Long> getAssignmentCountByTeacher(@PathVariable Long teacherId) {
        long count = teacherSubjectService.getAssignmentCountByTeacher(teacherId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get assignment count by subject
     * GET /api/assignments/count/subject/{subjectId}
     */
    @GetMapping("/count/subject/{subjectId}")
    public ResponseEntity<Long> getAssignmentCountBySubject(@PathVariable Long subjectId) {
        long count = teacherSubjectService.getAssignmentCountBySubject(subjectId);
        return ResponseEntity.ok(count);
    }
}