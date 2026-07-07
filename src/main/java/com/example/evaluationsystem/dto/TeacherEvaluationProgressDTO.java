// src/main/java/com/example/evaluationsystem/dto/TeacherEvaluationProgressDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherEvaluationProgressDTO {
    private Long teacherAssignmentId;
    private String teacherName;
    private String subjectName;
    private boolean evaluated;
    private Integer currentIndex;
    private Integer totalCount;
}