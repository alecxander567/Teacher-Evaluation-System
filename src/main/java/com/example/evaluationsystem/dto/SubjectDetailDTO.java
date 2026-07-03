// src/main/java/com/example/evaluationsystem/dto/SubjectDetailDTO.java
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
public class SubjectDetailDTO {
    private Long id;
    private Integer departmentId;
    private String departmentName;
    private String subjectCode;
    private String subjectName;
    private String description;
    private Long teacherCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}