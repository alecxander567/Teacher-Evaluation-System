package com.example.evaluationsystem.dto;

import com.example.evaluationsystem.model.EmploymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    private Long id;
    private Integer departmentId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String position;
    private EmploymentType employmentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}