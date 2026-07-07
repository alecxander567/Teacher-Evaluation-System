// src/main/java/com/example/evaluationsystem/dto/TeacherSelectionDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSelectionDTO {
    private Long teacherId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String position;
    private String employmentType;
    private Integer departmentId;
    private String departmentName;
    private Long subjectId;
    private String subjectName;
    private Long teacherAssignmentId;
    private boolean hasMultipleAssignments;
    private boolean selected;
}