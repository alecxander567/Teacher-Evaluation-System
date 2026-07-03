// src/main/java/com/example/evaluationsystem/dto/SubjectRequestDTO.java
package com.example.evaluationsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequestDTO {

    private Integer departmentId;

    @NotBlank(message = "Subject code is required")
    @Size(max = 50, message = "Subject code must not exceed 50 characters")
    private String subjectCode;

    @NotBlank(message = "Subject name is required")
    @Size(max = 255, message = "Subject name must not exceed 255 characters")
    private String subjectName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
}