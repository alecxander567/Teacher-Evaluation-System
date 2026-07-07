// src/main/java/com/example/evaluationsystem/dto/EvaluationBatchStatusRequestDTO.java
package com.example.evaluationsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationBatchStatusRequestDTO {

    @NotNull(message = "Evaluation period ID is required")
    private Long evaluationPeriodId;

    @Email(message = "Invalid email format")
    private String studentEmail;

    @NotEmpty(message = "At least one teacher assignment ID is required")
    private List<Long> teacherAssignmentIds;
}