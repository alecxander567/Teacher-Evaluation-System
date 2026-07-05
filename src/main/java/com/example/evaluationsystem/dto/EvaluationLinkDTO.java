// src/main/java/com/example/evaluationsystem/dto/EvaluationLinkDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationLinkDTO {
    private Long id;
    private String token;
    private String fullLink;
    private Long evaluationFormId;
    private String evaluationFormTitle;
    private String evaluationPeriodTitle;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer submissionCount;
}