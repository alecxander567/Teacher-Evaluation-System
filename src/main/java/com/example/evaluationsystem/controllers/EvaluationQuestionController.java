// src/main/java/com/example/evaluationsystem/controllers/EvaluationQuestionController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.EvaluationQuestionDTO;
import com.example.evaluationsystem.dto.EvaluationQuestionRequestDTO;
import com.example.evaluationsystem.service.EvaluationQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation-questions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EvaluationQuestionController {

    private final EvaluationQuestionService questionService;

    @PostMapping
    public ResponseEntity<EvaluationQuestionDTO> createQuestion(
            @Valid @RequestBody EvaluationQuestionRequestDTO requestDTO) {
        EvaluationQuestionDTO createdQuestion = questionService.createQuestion(requestDTO);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<EvaluationQuestionDTO>> getQuestionsByCategoryId(@PathVariable Long categoryId) {
        List<EvaluationQuestionDTO> questions = questionService.getQuestionsByCategoryId(categoryId);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/form/{formId}")
    public ResponseEntity<List<EvaluationQuestionDTO>> getQuestionsByFormId(@PathVariable Long formId) {
        List<EvaluationQuestionDTO> questions = questionService.getQuestionsByFormId(formId);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationQuestionDTO> getQuestionById(@PathVariable Long id) {
        EvaluationQuestionDTO question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationQuestionDTO> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationQuestionRequestDTO requestDTO) {
        EvaluationQuestionDTO updatedQuestion = questionService.updateQuestion(id, requestDTO);
        return ResponseEntity.ok(updatedQuestion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<Void> deleteQuestionsByCategoryId(@PathVariable Long categoryId) {
        questionService.deleteQuestionsByCategoryId(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}/count")
    public ResponseEntity<Long> countQuestionsByCategoryId(@PathVariable Long categoryId) {
        long count = questionService.countQuestionsByCategoryId(categoryId);
        return ResponseEntity.ok(count);
    }
}