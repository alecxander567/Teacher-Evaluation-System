// src/main/java/com/example/evaluationsystem/dto/EvaluationQuestionRequestDTO.java
package com.example.evaluationsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationQuestionRequestDTO {

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Question is required")
    @Size(max = 5000, message = "Question must not exceed 5000 characters")
    private String question;

    private Integer displayOrder = 0;

    private Boolean isRequired = true;
}