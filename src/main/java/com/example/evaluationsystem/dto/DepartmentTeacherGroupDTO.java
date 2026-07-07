// src/main/java/com/example/evaluationsystem/dto/DepartmentTeacherGroupDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentTeacherGroupDTO {
    private Integer departmentId;
    private String departmentName;
    private List<TeacherSelectionDTO> teachers;
}