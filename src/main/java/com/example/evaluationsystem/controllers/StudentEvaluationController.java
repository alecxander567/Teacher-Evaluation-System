// src/main/java/com/example/evaluationsystem/controllers/StudentEvaluationController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.*;
import com.example.evaluationsystem.service.StudentEvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/evaluation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentEvaluationController {

    private final StudentEvaluationService studentEvaluationService;

    /**
     * Get available teachers grouped by department for a specific evaluation period
     * GET /api/student/evaluation/teachers?periodId={periodId}&formId={formId}&studentEmail={studentEmail}
     */
    @GetMapping("/teachers")
    public ResponseEntity<List<DepartmentTeacherGroupDTO>> getAvailableTeachers(
            @RequestParam Long periodId,
            @RequestParam Long formId,
            @RequestParam String studentEmail) {
        List<DepartmentTeacherGroupDTO> teacherGroups = 
                studentEvaluationService.getAvailableTeachersForPeriod(periodId, formId, studentEmail);
        return ResponseEntity.ok(teacherGroups);
    }

    /**
     * Start a new evaluation session with selected teachers
     * POST /api/student/evaluation/session/start
     */
    @PostMapping("/session/start")
    public ResponseEntity<StudentEvaluationSessionDTO> startEvaluationSession(
            @Valid @RequestBody StartEvaluationSessionRequestDTO request) {
        StudentEvaluationSessionDTO session = 
                studentEvaluationService.startEvaluationSession(request);
        return new ResponseEntity<>(session, HttpStatus.CREATED);
    }

    /**
     * Get current evaluation session status
     * GET /api/student/evaluation/session/{sessionId}
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<StudentEvaluationSessionDTO> getEvaluationSession(
            @PathVariable String sessionId) {
        StudentEvaluationSessionDTO session = 
                studentEvaluationService.getEvaluationSession(sessionId);
        return ResponseEntity.ok(session);
    }

    /**
     * Get the next teacher to evaluate
     * GET /api/student/evaluation/session/{sessionId}/next
     */
    @GetMapping("/session/{sessionId}/next")
    public ResponseEntity<TeacherEvaluationProgressDTO> getNextTeacher(
            @PathVariable String sessionId) {
        TeacherEvaluationProgressDTO nextTeacher = 
                studentEvaluationService.getNextTeacherToEvaluate(sessionId);
        return ResponseEntity.ok(nextTeacher);
    }

    /**
     * Get progress for the current session
     * GET /api/student/evaluation/session/{sessionId}/progress
     */
    @GetMapping("/session/{sessionId}/progress")
    public ResponseEntity<List<TeacherEvaluationProgressDTO>> getSessionProgress(
            @PathVariable String sessionId) {
        List<TeacherEvaluationProgressDTO> progress = 
                studentEvaluationService.getSessionProgress(sessionId);
        return ResponseEntity.ok(progress);
    }

    /**
     * Check if session is complete
     * GET /api/student/evaluation/session/{sessionId}/is-complete
     */
    @GetMapping("/session/{sessionId}/is-complete")
    public ResponseEntity<Boolean> isSessionComplete(@PathVariable String sessionId) {
        boolean complete = studentEvaluationService.isSessionComplete(sessionId);
        return ResponseEntity.ok(complete);
    }

    /**
     * Get evaluation form for a specific teacher in the session
     * GET /api/student/evaluation/session/{sessionId}/teacher/{teacherAssignmentId}/form
     */
    @GetMapping("/session/{sessionId}/teacher/{teacherAssignmentId}/form")
    public ResponseEntity<EvaluationFormDetailDTO> getTeacherEvaluationForm(
            @PathVariable String sessionId,
            @PathVariable Long teacherAssignmentId) {
        EvaluationFormDetailDTO form = 
                studentEvaluationService.getTeacherEvaluationForm(sessionId, teacherAssignmentId);
        return ResponseEntity.ok(form);
    }

    /**
     * Submit evaluation for a teacher and move to next
     * POST /api/student/evaluation/session/{sessionId}/submit
     */
    @PostMapping("/session/{sessionId}/submit")
    public ResponseEntity<TeacherEvaluationProgressDTO> submitTeacherEvaluation(
            @PathVariable String sessionId,
            @Valid @RequestBody EvaluationSubmissionRequestDTO submissionRequest) {
        TeacherEvaluationProgressDTO nextTeacher = 
                studentEvaluationService.submitTeacherEvaluation(sessionId, submissionRequest);
        return ResponseEntity.ok(nextTeacher);
    }

    /**
     * Complete the evaluation session
     * POST /api/student/evaluation/session/{sessionId}/complete
     */
    @PostMapping("/session/{sessionId}/complete")
    public ResponseEntity<Void> completeEvaluationSession(
            @PathVariable String sessionId) {
        studentEvaluationService.completeEvaluationSession(sessionId);
        return ResponseEntity.ok().build();
    }

    /**
     * Cancel the evaluation session
     * DELETE /api/student/evaluation/session/{sessionId}
     */
    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> cancelEvaluationSession(
            @PathVariable String sessionId) {
        studentEvaluationService.cancelEvaluationSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}