// src/main/java/com/example/evaluationsystem/dto/EvaluationQuestionDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationQuestionDTO {
    private Long id;
    private Long categoryId;
    private String question;
    private Integer displayOrder;
    private Boolean isRequired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}