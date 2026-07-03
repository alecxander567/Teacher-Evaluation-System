// src/main/java/com/example/evaluationsystem/dto/TeacherSubjectDetailDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSubjectDetailDTO {
    private Long id;
    private Long teacherId;
    private String teacherName;
    private String teacherEmail;
    private Long subjectId;
    private String subjectCode;
    private String subjectName;
    private String subjectDescription;
    private String academicYear;
    private String semester;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}