// src/main/java/com/example/evaluationsystem/dto/EvaluationFormDetailDTO.java (Updated)
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationFormDetailDTO {
    private Long id;
    private Long evaluationPeriodId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String evaluationPeriodTitle;
    private String evaluationPeriodStatus;
    private boolean isPeriodActive;
    private Integer totalQuestions;
    private Integer totalResponses;
    private List<EvaluationCategoryDTO> categories;
}