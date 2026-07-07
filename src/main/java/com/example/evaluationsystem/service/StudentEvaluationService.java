// src/main/java/com/example/evaluationsystem/service/StudentEvaluationService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.*;

import java.util.List;

public interface StudentEvaluationService {
    
    List<DepartmentTeacherGroupDTO> getAvailableTeachersForPeriod(Long periodId, Long formId, String studentEmail);
    
    StudentEvaluationSessionDTO startEvaluationSession(StartEvaluationSessionRequestDTO request);
    
    StudentEvaluationSessionDTO getEvaluationSession(String sessionId);
    
    TeacherEvaluationProgressDTO getNextTeacherToEvaluate(String sessionId);
    
    List<TeacherEvaluationProgressDTO> getSessionProgress(String sessionId);
    
    boolean isSessionComplete(String sessionId);
    
    EvaluationFormDetailDTO getTeacherEvaluationForm(String sessionId, Long teacherAssignmentId);
    
    TeacherEvaluationProgressDTO submitTeacherEvaluation(String sessionId, EvaluationSubmissionRequestDTO submissionRequest);
    
    void completeEvaluationSession(String sessionId);
    
    void cancelEvaluationSession(String sessionId);
}