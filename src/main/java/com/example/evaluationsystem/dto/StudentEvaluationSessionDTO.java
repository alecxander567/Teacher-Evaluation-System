// src/main/java/com/example/evaluationsystem/dto/StudentEvaluationSessionDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEvaluationSessionDTO {
    private String sessionId;
    private String studentEmail;
    private String studentName;
    private Long evaluationPeriodId;
    private String evaluationPeriodTitle;
    private Long evaluationFormId;
    private String evaluationFormTitle;
    private List<Long> selectedTeacherAssignmentIds;
    private Integer currentIndex;
    private Long currentTeacherAssignmentId;
    private Integer totalTeachers;
    private Integer completedCount;
    private String status; // "SELECTING", "IN_PROGRESS", "COMPLETED"
}