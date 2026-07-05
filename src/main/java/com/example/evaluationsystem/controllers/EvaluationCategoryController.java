// src/main/java/com/example/evaluationsystem/controllers/EvaluationCategoryController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.EvaluationCategoryDTO;
import com.example.evaluationsystem.dto.EvaluationCategoryRequestDTO;
import com.example.evaluationsystem.service.EvaluationCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation-categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EvaluationCategoryController {

    private final EvaluationCategoryService categoryService;

    @PostMapping
    public ResponseEntity<EvaluationCategoryDTO> createCategory(
            @Valid @RequestBody EvaluationCategoryRequestDTO requestDTO) {
        EvaluationCategoryDTO createdCategory = categoryService.createCategory(requestDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/form/{formId}")
    public ResponseEntity<List<EvaluationCategoryDTO>> getCategoriesByFormId(@PathVariable Long formId) {
        List<EvaluationCategoryDTO> categories = categoryService.getCategoriesByFormId(formId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/form/{formId}/with-questions")
    public ResponseEntity<List<EvaluationCategoryDTO>> getCategoriesWithQuestionsByFormId(@PathVariable Long formId) {
        List<EvaluationCategoryDTO> categories = categoryService.getCategoriesWithQuestionsByFormId(formId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationCategoryDTO> getCategoryById(@PathVariable Long id) {
        EvaluationCategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationCategoryDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationCategoryRequestDTO requestDTO) {
        EvaluationCategoryDTO updatedCategory = categoryService.updateCategory(id, requestDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/form/{formId}")
    public ResponseEntity<Void> deleteCategoriesByFormId(@PathVariable Long formId) {
        categoryService.deleteCategoriesByFormId(formId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/form/{formId}/exists")
    public ResponseEntity<Boolean> existsCategoriesForForm(@PathVariable Long formId) {
        boolean exists = categoryService.existsCategoriesForForm(formId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/form/{formId}/count")
    public ResponseEntity<Long> countCategoriesByFormId(@PathVariable Long formId) {
        long count = categoryService.countCategoriesByFormId(formId);
        return ResponseEntity.ok(count);
    }
}