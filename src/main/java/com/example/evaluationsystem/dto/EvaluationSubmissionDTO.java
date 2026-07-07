// src/main/java/com/example/evaluationsystem/dto/EvaluationSubmissionDTO.java
package com.example.evaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationSubmissionDTO {
    private Long id;
    private Long evaluationPeriodId;
    private Long teacherAssignmentId;
    private Long evaluationLinkId;
    private String studentEmail;
    private String overallComment;
    private LocalDateTime submittedAt;
    private String evaluationPeriodTitle;
    private String teacherName;
    private String subjectName;
    private Integer totalQuestions;
    private Integer answeredQuestions;
    private Double averageRating;
    private List<EvaluationResponseDTO> responses;
}