// src/main/java/com/example/evaluationsystem/dto/TeacherEvaluationStatusDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherEvaluationStatusDTO {
    private Long teacherId;
    private Long teacherAssignmentId;
    private String teacherName;
    private boolean evaluated;
    private Long submissionId; // null if not yet evaluated
}