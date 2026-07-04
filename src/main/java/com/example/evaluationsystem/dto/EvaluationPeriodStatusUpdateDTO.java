// src/main/java/com/example/evaluationsystem/dto/EvaluationPeriodStatusUpdateDTO.java
package com.example.evaluationsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationPeriodStatusUpdateDTO {
    
    @NotBlank(message = "Status is required")
    private String status;
}