// src/main/java/com/example/evaluationsystem/service/EvaluationSubmissionService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.EvaluationBatchStatusRequestDTO;
import com.example.evaluationsystem.dto.EvaluationSubmissionDTO;
import com.example.evaluationsystem.dto.EvaluationSubmissionRequestDTO;
import com.example.evaluationsystem.dto.TeacherEvaluationStatusDTO;

import java.util.List;

public interface EvaluationSubmissionService {
    EvaluationSubmissionDTO createSubmission(EvaluationSubmissionRequestDTO requestDTO);
    EvaluationSubmissionDTO getSubmissionById(Long id);
    EvaluationSubmissionDTO getSubmissionWithResponses(Long id);
    List<EvaluationSubmissionDTO> getSubmissionsByPeriodId(Long periodId);
    List<EvaluationSubmissionDTO> getSubmissionsByAssignmentId(Long assignmentId);
    List<EvaluationSubmissionDTO> getSubmissionsByStudentEmail(String email);
    boolean hasStudentSubmitted(Long periodId, Long assignmentId, String studentEmail);
    long countSubmissionsByPeriodId(Long periodId);
    long countSubmissionsByAssignmentId(Long assignmentId);
    void deleteSubmission(Long id);
    List<TeacherEvaluationStatusDTO> checkBatchSubmissionStatus(EvaluationBatchStatusRequestDTO requestDTO);
}