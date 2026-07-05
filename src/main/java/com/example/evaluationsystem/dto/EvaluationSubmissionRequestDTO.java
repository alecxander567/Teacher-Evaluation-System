// src/main/java/com/example/evaluationsystem/dto/EvaluationSubmissionRequestDTO.java
package com.example.evaluationsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationSubmissionRequestDTO {

    @NotNull(message = "Evaluation period ID is required")
    private Long evaluationPeriodId;

    @NotNull(message = "Teacher assignment ID is required")
    private Long teacherAssignmentId;

    @Email(message = "Invalid email format")
    private String studentEmail;

    private String overallComment;

    private List<EvaluationResponseRequestDTO> responses;
}