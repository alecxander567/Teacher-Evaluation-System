// src/main/java/com/example/evaluationsystem/service/EvaluationFormService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.EvaluationFormDTO;
import com.example.evaluationsystem.dto.EvaluationFormDetailDTO;
import com.example.evaluationsystem.dto.EvaluationFormRequestDTO;

import java.util.List;

public interface EvaluationFormService {
    EvaluationFormDTO createEvaluationForm(EvaluationFormRequestDTO requestDTO);
    EvaluationFormDTO updateEvaluationForm(Long id, EvaluationFormRequestDTO requestDTO);
    void deleteEvaluationForm(Long id);
    void deleteFormsByEvaluationPeriod(Long evaluationPeriodId);
    EvaluationFormDTO getEvaluationFormById(Long id);
    EvaluationFormDetailDTO getEvaluationFormDetailById(Long id);
    List<EvaluationFormDTO> getAllEvaluationForms();
    List<EvaluationFormDTO> getFormsByEvaluationPeriodId(Long evaluationPeriodId);
    List<EvaluationFormDTO> searchFormsByTitle(String title);
    List<EvaluationFormDTO> getFormsByPeriodStatus(String status);
    boolean existsEvaluationForm(Long id);
    boolean existsFormsForPeriod(Long evaluationPeriodId);
    long countFormsByPeriodId(Long evaluationPeriodId);
    List<EvaluationFormDTO> getFormsWithPeriodDetails();
}