// src/main/java/com/example/evaluationsystem/controllers/EvaluationFormController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.EvaluationFormDTO;
import com.example.evaluationsystem.dto.EvaluationFormDetailDTO;
import com.example.evaluationsystem.dto.EvaluationFormRequestDTO;
import com.example.evaluationsystem.service.EvaluationFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation-forms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EvaluationFormController {

    private final EvaluationFormService evaluationFormService;

    /**
     * Create a new evaluation form
     * POST /api/evaluation-forms
     */
    @PostMapping
    public ResponseEntity<EvaluationFormDTO> createEvaluationForm(
            @Valid @RequestBody EvaluationFormRequestDTO requestDTO) {
        EvaluationFormDTO createdForm = evaluationFormService.createEvaluationForm(requestDTO);
        return new ResponseEntity<>(createdForm, HttpStatus.CREATED);
    }

    /**
     * Get all evaluation forms
     * GET /api/evaluation-forms
     */
    @GetMapping
    public ResponseEntity<List<EvaluationFormDTO>> getAllEvaluationForms() {
        List<EvaluationFormDTO> forms = evaluationFormService.getAllEvaluationForms();
        return ResponseEntity.ok(forms);
    }

    /**
     * Get evaluation form by ID
     * GET /api/evaluation-forms/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationFormDTO> getEvaluationFormById(@PathVariable Long id) {
        EvaluationFormDTO form = evaluationFormService.getEvaluationFormById(id);
        return ResponseEntity.ok(form);
    }

    /**
     * Get evaluation form details with computed fields
     * GET /api/evaluation-forms/{id}/details
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<EvaluationFormDetailDTO> getEvaluationFormDetailById(@PathVariable Long id) {
        EvaluationFormDetailDTO formDetail = evaluationFormService.getEvaluationFormDetailById(id);
        return ResponseEntity.ok(formDetail);
    }

    /**
     * Get forms by evaluation period ID
     * GET /api/evaluation-forms/period/{periodId}
     */
    @GetMapping("/period/{periodId}")
    public ResponseEntity<List<EvaluationFormDTO>> getFormsByEvaluationPeriodId(@PathVariable Long periodId) {
        List<EvaluationFormDTO> forms = evaluationFormService.getFormsByEvaluationPeriodId(periodId);
        return ResponseEntity.ok(forms);
    }

    /**
     * Get forms by period status
     * GET /api/evaluation-forms/period-status/{status}
     */
    @GetMapping("/period-status/{status}")
    public ResponseEntity<List<EvaluationFormDTO>> getFormsByPeriodStatus(@PathVariable String status) {
        List<EvaluationFormDTO> forms = evaluationFormService.getFormsByPeriodStatus(status);
        return ResponseEntity.ok(forms);
    }

    /**
     * Search forms by title
     * GET /api/evaluation-forms/search?title={title}
     */
    @GetMapping("/search")
    public ResponseEntity<List<EvaluationFormDTO>> searchFormsByTitle(@RequestParam String title) {
        List<EvaluationFormDTO> forms = evaluationFormService.searchFormsByTitle(title);
        return ResponseEntity.ok(forms);
    }

    /**
     * Get forms with period details
     * GET /api/evaluation-forms/with-period-details
     */
    @GetMapping("/with-period-details")
    public ResponseEntity<List<EvaluationFormDTO>> getFormsWithPeriodDetails() {
        List<EvaluationFormDTO> forms = evaluationFormService.getFormsWithPeriodDetails();
        return ResponseEntity.ok(forms);
    }

    /**
     * Update evaluation form
     * PUT /api/evaluation-forms/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<EvaluationFormDTO> updateEvaluationForm(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationFormRequestDTO requestDTO) {
        EvaluationFormDTO updatedForm = evaluationFormService.updateEvaluationForm(id, requestDTO);
        return ResponseEntity.ok(updatedForm);
    }

    /**
     * Delete evaluation form
     * DELETE /api/evaluation-forms/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluationForm(@PathVariable Long id) {
        evaluationFormService.deleteEvaluationForm(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all forms for a specific evaluation period
     * DELETE /api/evaluation-forms/period/{periodId}
     */
    @DeleteMapping("/period/{periodId}")
    public ResponseEntity<Void> deleteFormsByEvaluationPeriod(@PathVariable Long periodId) {
        evaluationFormService.deleteFormsByEvaluationPeriod(periodId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if form exists
     * GET /api/evaluation-forms/{id}/exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsEvaluationForm(@PathVariable Long id) {
        boolean exists = evaluationFormService.existsEvaluationForm(id);
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if forms exist for a period
     * GET /api/evaluation-forms/period/{periodId}/exists
     */
    @GetMapping("/period/{periodId}/exists")
    public ResponseEntity<Boolean> existsFormsForPeriod(@PathVariable Long periodId) {
        boolean exists = evaluationFormService.existsFormsForPeriod(periodId);
        return ResponseEntity.ok(exists);
    }

    /**
     * Count forms by period ID
     * GET /api/evaluation-forms/period/{periodId}/count
     */
    @GetMapping("/period/{periodId}/count")
    public ResponseEntity<Long> countFormsByPeriodId(@PathVariable Long periodId) {
        long count = evaluationFormService.countFormsByPeriodId(periodId);
        return ResponseEntity.ok(count);
    }
}