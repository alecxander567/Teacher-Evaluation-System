// src/main/java/com/example/evaluationsystem/dto/StartEvaluationSessionRequestDTO.java
package com.example.evaluationsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartEvaluationSessionRequestDTO {
    
    @NotNull(message = "Evaluation period ID is required")
    private Long evaluationPeriodId;
    
    @NotNull(message = "Evaluation form ID is required")
    private Long evaluationFormId;
    
    @NotBlank(message = "Student email is required")
    @Email(message = "Invalid email format")
    private String studentEmail;
    
    private String studentName;
    
    @NotNull(message = "At least one teacher must be selected")
    private List<Long> teacherAssignmentIds;
}