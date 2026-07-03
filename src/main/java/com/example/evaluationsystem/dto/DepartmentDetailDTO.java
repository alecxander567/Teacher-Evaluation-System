// src/main/java/com/example/evaluationsystem/dto/DepartmentDetailDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDetailDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer teacherCount;
    private List<TeacherDTO> teachers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}