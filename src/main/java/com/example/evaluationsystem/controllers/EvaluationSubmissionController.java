// src/main/java/com/example/evaluationsystem/controllers/EvaluationSubmissionController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.EvaluationSubmissionDTO;
import com.example.evaluationsystem.dto.EvaluationSubmissionRequestDTO;
import com.example.evaluationsystem.service.EvaluationSubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation-submissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EvaluationSubmissionController {

    private final EvaluationSubmissionService submissionService;

    @PostMapping
    public ResponseEntity<EvaluationSubmissionDTO> createSubmission(
            @Valid @RequestBody EvaluationSubmissionRequestDTO requestDTO) {
        EvaluationSubmissionDTO created = submissionService.createSubmission(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationSubmissionDTO> getSubmissionById(@PathVariable Long id) {
        EvaluationSubmissionDTO submission = submissionService.getSubmissionById(id);
        return ResponseEntity.ok(submission);
    }

    @GetMapping("/{id}/with-responses")
    public ResponseEntity<EvaluationSubmissionDTO> getSubmissionWithResponses(@PathVariable Long id) {
        EvaluationSubmissionDTO submission = submissionService.getSubmissionWithResponses(id);
        return ResponseEntity.ok(submission);
    }

    @GetMapping("/period/{periodId}")
    public ResponseEntity<List<EvaluationSubmissionDTO>> getSubmissionsByPeriodId(@PathVariable Long periodId) {
        List<EvaluationSubmissionDTO> submissions = submissionService.getSubmissionsByPeriodId(periodId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<EvaluationSubmissionDTO>> getSubmissionsByAssignmentId(@PathVariable Long assignmentId) {
        List<EvaluationSubmissionDTO> submissions = submissionService.getSubmissionsByAssignmentId(assignmentId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/student/{email}")
    public ResponseEntity<List<EvaluationSubmissionDTO>> getSubmissionsByStudentEmail(@PathVariable String email) {
        List<EvaluationSubmissionDTO> submissions = submissionService.getSubmissionsByStudentEmail(email);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/check-submission")
    public ResponseEntity<Boolean> hasStudentSubmitted(
            @RequestParam Long periodId,
            @RequestParam Long assignmentId,
            @RequestParam String studentEmail) {
        boolean hasSubmitted = submissionService.hasStudentSubmitted(periodId, assignmentId, studentEmail);
        return ResponseEntity.ok(hasSubmitted);
    }

    @GetMapping("/period/{periodId}/count")
    public ResponseEntity<Long> countSubmissionsByPeriodId(@PathVariable Long periodId) {
        long count = submissionService.countSubmissionsByPeriodId(periodId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/assignment/{assignmentId}/count")
    public ResponseEntity<Long> countSubmissionsByAssignmentId(@PathVariable Long assignmentId) {
        long count = submissionService.countSubmissionsByAssignmentId(assignmentId);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }
}