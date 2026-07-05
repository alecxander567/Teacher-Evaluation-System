// src/main/java/com/example/evaluationsystem/dto/EvaluationCategoryRequestDTO.java
package com.example.evaluationsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationCategoryRequestDTO {

    @NotNull(message = "Form ID is required")
    private Long formId;

    @NotBlank(message = "Category name is required")
    @Size(max = 255, message = "Category name must not exceed 255 characters")
    private String name;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    private Integer displayOrder = 0;

    private List<EvaluationQuestionRequestDTO> questions;
}