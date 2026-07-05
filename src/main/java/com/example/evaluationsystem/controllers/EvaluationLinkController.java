// src/main/java/com/example/evaluationsystem/controllers/EvaluationLinkController.java
package com.example.evaluationsystem.controllers;

import com.example.evaluationsystem.dto.EvaluationLinkDTO;
import com.example.evaluationsystem.dto.EvaluationLinkRequestDTO;
import com.example.evaluationsystem.service.EvaluationLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation-links")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EvaluationLinkController {

    private final EvaluationLinkService evaluationLinkService;

    @PostMapping
    public ResponseEntity<EvaluationLinkDTO> createLink(
            @Valid @RequestBody EvaluationLinkRequestDTO requestDTO) {
        EvaluationLinkDTO created = evaluationLinkService.createLink(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EvaluationLinkDTO>> getAllLinks() {
        List<EvaluationLinkDTO> links = evaluationLinkService.getAllLinks();
        return ResponseEntity.ok(links);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationLinkDTO> getLinkById(@PathVariable Long id) {
        EvaluationLinkDTO link = evaluationLinkService.getLinkById(id);
        return ResponseEntity.ok(link);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<EvaluationLinkDTO> getLinkByToken(@PathVariable String token) {
        EvaluationLinkDTO link = evaluationLinkService.getLinkByToken(token);
        return ResponseEntity.ok(link);
    }

    // Changed from period to form
    @GetMapping("/form/{formId}")
    public ResponseEntity<List<EvaluationLinkDTO>> getLinksByFormId(@PathVariable Long formId) {
        List<EvaluationLinkDTO> links = evaluationLinkService.getLinksByFormId(formId);
        return ResponseEntity.ok(links);
    }

    @GetMapping("/validate/{token}")
    public ResponseEntity<Boolean> validateToken(@PathVariable String token) {
        boolean valid = evaluationLinkService.isValidToken(token);
        return ResponseEntity.ok(valid);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateLink(@PathVariable Long id) {
        evaluationLinkService.deactivateLink(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable Long id) {
        evaluationLinkService.deleteLink(id);
        return ResponseEntity.noContent().build();
    }
}