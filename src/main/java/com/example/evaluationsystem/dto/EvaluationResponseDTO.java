// src/main/java/com/example/evaluationsystem/dto/EvaluationResponseDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResponseDTO {
    private Long id;
    private Long submissionId;
    private Long questionId;
    private String questionText;
    private Integer rating;
    private LocalDateTime createdAt;
}