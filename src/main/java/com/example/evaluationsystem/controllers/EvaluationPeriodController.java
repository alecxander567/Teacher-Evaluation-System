// src/main/java/com/example/evaluationsystem/controllers/EvaluationPeriodController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.EvaluationPeriodDTO;
import com.example.evaluationsystem.dto.EvaluationPeriodDetailDTO;
import com.example.evaluationsystem.dto.EvaluationPeriodRequestDTO;
import com.example.evaluationsystem.dto.EvaluationPeriodStatusUpdateDTO;
import com.example.evaluationsystem.service.EvaluationPeriodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation-periods")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EvaluationPeriodController {

    private final EvaluationPeriodService evaluationPeriodService;

    /**
     * Create a new evaluation period
     * POST /api/evaluation-periods
     */
    @PostMapping
    public ResponseEntity<EvaluationPeriodDTO> createEvaluationPeriod(
            @Valid @RequestBody EvaluationPeriodRequestDTO requestDTO) {
        EvaluationPeriodDTO createdPeriod = evaluationPeriodService.createEvaluationPeriod(requestDTO);
        return new ResponseEntity<>(createdPeriod, HttpStatus.CREATED);
    }

    /**
     * Get all evaluation periods
     * GET /api/evaluation-periods
     */
    @GetMapping
    public ResponseEntity<List<EvaluationPeriodDTO>> getAllEvaluationPeriods() {
        List<EvaluationPeriodDTO> periods = evaluationPeriodService.getAllEvaluationPeriods();
        return ResponseEntity.ok(periods);
    }

    /**
     * Get evaluation period by ID
     * GET /api/evaluation-periods/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationPeriodDTO> getEvaluationPeriodById(@PathVariable Long id) {
        EvaluationPeriodDTO period = evaluationPeriodService.getEvaluationPeriodById(id);
        return ResponseEntity.ok(period);
    }

    /**
     * Get evaluation period details with computed fields
     * GET /api/evaluation-periods/{id}/details
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<EvaluationPeriodDetailDTO> getEvaluationPeriodDetailById(@PathVariable Long id) {
        EvaluationPeriodDetailDTO periodDetail = evaluationPeriodService.getEvaluationPeriodDetailById(id);
        return ResponseEntity.ok(periodDetail);
    }

    /**
     * Get periods by status
     * GET /api/evaluation-periods/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EvaluationPeriodDTO>> getEvaluationPeriodsByStatus(@PathVariable String status) {
        List<EvaluationPeriodDTO> periods = evaluationPeriodService.getEvaluationPeriodsByStatus(status);
        return ResponseEntity.ok(periods);
    }

    /**
     * Get periods by academic year and semester
     * GET /api/evaluation-periods/academic-year/{academicYear}/semester/{semester}
     */
    @GetMapping("/academic-year/{academicYear}/semester/{semester}")
    public ResponseEntity<List<EvaluationPeriodDTO>> getEvaluationPeriodsByAcademicYearAndSemester(
            @PathVariable String academicYear,
            @PathVariable String semester) {
        List<EvaluationPeriodDTO> periods = evaluationPeriodService
                .getEvaluationPeriodsByAcademicYearAndSemester(academicYear, semester);
        return ResponseEntity.ok(periods);
    }

    /**
     * Get active periods
     * GET /api/evaluation-periods/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<EvaluationPeriodDTO>> getActiveEvaluationPeriods() {
        List<EvaluationPeriodDTO> periods = evaluationPeriodService.getActiveEvaluationPeriods();
        return ResponseEntity.ok(periods);
    }

    /**
     * Get upcoming periods
     * GET /api/evaluation-periods/upcoming
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<EvaluationPeriodDTO>> getUpcomingEvaluationPeriods() {
        List<EvaluationPeriodDTO> periods = evaluationPeriodService.getUpcomingEvaluationPeriods();
        return ResponseEntity.ok(periods);
    }

    /**
     * Get past periods
     * GET /api/evaluation-periods/past
     */
    @GetMapping("/past")
    public ResponseEntity<List<EvaluationPeriodDTO>> getPastEvaluationPeriods() {
        List<EvaluationPeriodDTO> periods = evaluationPeriodService.getPastEvaluationPeriods();
        return ResponseEntity.ok(periods);
    }

    /**
     * Update evaluation period
     * PUT /api/evaluation-periods/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<EvaluationPeriodDTO> updateEvaluationPeriod(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationPeriodRequestDTO requestDTO) {
        EvaluationPeriodDTO updatedPeriod = evaluationPeriodService.updateEvaluationPeriod(id, requestDTO);
        return ResponseEntity.ok(updatedPeriod);
    }

    /**
     * Update evaluation period status
     * PATCH /api/evaluation-periods/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<EvaluationPeriodDTO> updateEvaluationPeriodStatus(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationPeriodStatusUpdateDTO statusUpdateDTO) {
        EvaluationPeriodDTO updatedPeriod = evaluationPeriodService.updateEvaluationPeriodStatus(id, statusUpdateDTO);
        return ResponseEntity.ok(updatedPeriod);
    }

    /**
     * Delete evaluation period
     * DELETE /api/evaluation-periods/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluationPeriod(@PathVariable Long id) {
        evaluationPeriodService.deleteEvaluationPeriod(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if period exists
     * GET /api/evaluation-periods/{id}/exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsEvaluationPeriod(@PathVariable Long id) {
        boolean exists = evaluationPeriodService.existsEvaluationPeriod(id);
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if period is active
     * GET /api/evaluation-periods/{id}/is-active
     */
    @GetMapping("/{id}/is-active")
    public ResponseEntity<Boolean> isPeriodActive(@PathVariable Long id) {
        boolean isActive = evaluationPeriodService.isPeriodActive(id);
        return ResponseEntity.ok(isActive);
    }

    /**
     * Check if there's an active period for a semester
     * GET /api/evaluation-periods/has-active-period?academicYear={academicYear}&semester={semester}
     */
    @GetMapping("/has-active-period")
    public ResponseEntity<Boolean> hasActivePeriodForSemester(
            @RequestParam String academicYear,
            @RequestParam String semester) {
        boolean hasActive = evaluationPeriodService.hasActivePeriodForSemester(academicYear, semester);
        return ResponseEntity.ok(hasActive);
    }

    /**
     * Get count of periods by status
     * GET /api/evaluation-periods/count/status/{status}
     */
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getCountByStatus(@PathVariable String status) {
        long count = evaluationPeriodService.getCountByStatus(status);
        return ResponseEntity.ok(count);
    }
}