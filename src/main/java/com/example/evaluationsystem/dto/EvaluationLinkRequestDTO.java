// src/main/java/com/example/evaluationsystem/dto/EvaluationLinkRequestDTO.java
package com.example.evaluationsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationLinkRequestDTO {

    @NotNull(message = "Evaluation form ID is required")
    private Long evaluationFormId;
}