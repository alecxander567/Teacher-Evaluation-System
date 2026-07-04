// src/main/java/com/example/evaluationsystem/service/EvaluationPeriodService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.EvaluationPeriodDTO;
import com.example.evaluationsystem.dto.EvaluationPeriodDetailDTO;
import com.example.evaluationsystem.dto.EvaluationPeriodRequestDTO;
import com.example.evaluationsystem.dto.EvaluationPeriodStatusUpdateDTO;

import java.util.List;

public interface EvaluationPeriodService {
    EvaluationPeriodDTO createEvaluationPeriod(EvaluationPeriodRequestDTO requestDTO);
    EvaluationPeriodDTO updateEvaluationPeriod(Long id, EvaluationPeriodRequestDTO requestDTO);
    void deleteEvaluationPeriod(Long id);
    EvaluationPeriodDTO getEvaluationPeriodById(Long id);
    EvaluationPeriodDetailDTO getEvaluationPeriodDetailById(Long id);
    List<EvaluationPeriodDTO> getAllEvaluationPeriods();
    List<EvaluationPeriodDTO> getEvaluationPeriodsByStatus(String status);
    List<EvaluationPeriodDTO> getEvaluationPeriodsByAcademicYearAndSemester(String academicYear, String semester);
    List<EvaluationPeriodDTO> getActiveEvaluationPeriods();
    List<EvaluationPeriodDTO> getUpcomingEvaluationPeriods();
    List<EvaluationPeriodDTO> getPastEvaluationPeriods();
    EvaluationPeriodDTO updateEvaluationPeriodStatus(Long id, EvaluationPeriodStatusUpdateDTO statusUpdateDTO);
    boolean existsEvaluationPeriod(Long id);
    long getCountByStatus(String status);
    boolean isPeriodActive(Long id);
    boolean hasActivePeriodForSemester(String academicYear, String semester);
}