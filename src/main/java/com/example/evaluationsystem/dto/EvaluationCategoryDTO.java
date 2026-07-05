// src/main/java/com/example/evaluationsystem/dto/EvaluationCategoryDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationCategoryDTO {
    private Long id;
    private Long formId;
    private String name;
    private String description;
    private Integer displayOrder;
    private Long questionCount; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<EvaluationQuestionDTO> questions;
}