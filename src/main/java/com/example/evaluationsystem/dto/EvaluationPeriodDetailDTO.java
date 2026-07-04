// src/main/java/com/example/evaluationsystem/dto/EvaluationPeriodDetailDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationPeriodDetailDTO {
    private Long id;
    private String title;
    private String academicYear;
    private String semester;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private long daysRemaining;
    private boolean isActive;
    private boolean isUpcoming;
    private boolean isPast;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}