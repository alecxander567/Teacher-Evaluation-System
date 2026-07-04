// src/main/java/com/example/evaluationsystem/service/impl/EvaluationPeriodServiceImpl.java
package com.example.evaluationsystem.service.impl;

import com.example.evaluationsystem.dto.EvaluationPeriodDTO;
import com.example.evaluationsystem.dto.EvaluationPeriodDetailDTO;
import com.example.evaluationsystem.dto.EvaluationPeriodRequestDTO;
import com.example.evaluationsystem.dto.EvaluationPeriodStatusUpdateDTO;
import com.example.evaluationsystem.model.EvaluationPeriod;
import com.example.evaluationsystem.repository.EvaluationPeriodRepository;
import com.example.evaluationsystem.service.EvaluationPeriodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvaluationPeriodServiceImpl implements EvaluationPeriodService {

    private final EvaluationPeriodRepository evaluationPeriodRepository;

    @Override
    @Transactional
    public EvaluationPeriodDTO createEvaluationPeriod(EvaluationPeriodRequestDTO requestDTO) {
        log.info("Creating new evaluation period: {}", requestDTO.getTitle());
        
        // Only validate that start date is before end date
        if (requestDTO.getStartDate().isAfter(requestDTO.getEndDate())) {
            throw new RuntimeException("Start date must be before end date");
        }
        
        EvaluationPeriod period = new EvaluationPeriod();
        period.setTitle(requestDTO.getTitle());
        period.setAcademicYear(requestDTO.getAcademicYear());
        period.setSemester(requestDTO.getSemester());
        period.setStartDate(requestDTO.getStartDate());
        period.setEndDate(requestDTO.getEndDate());
        period.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : "draft");
        
        EvaluationPeriod savedPeriod = evaluationPeriodRepository.save(period);
        log.info("Evaluation period created successfully with ID: {}", savedPeriod.getId());
        
        return convertToDTO(savedPeriod);
    }

    @Override
    @Transactional
    public EvaluationPeriodDTO updateEvaluationPeriod(Long id, EvaluationPeriodRequestDTO requestDTO) {
        log.info("Updating evaluation period with ID: {}", id);
        
        EvaluationPeriod period = evaluationPeriodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation period not found with ID: " + id));
        
        // Only validate that start date is before end date
        if (requestDTO.getStartDate().isAfter(requestDTO.getEndDate())) {
            throw new RuntimeException("Start date must be before end date");
        }
        
        period.setTitle(requestDTO.getTitle());
        period.setAcademicYear(requestDTO.getAcademicYear());
        period.setSemester(requestDTO.getSemester());
        period.setStartDate(requestDTO.getStartDate());
        period.setEndDate(requestDTO.getEndDate());
        period.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : period.getStatus());
        
        EvaluationPeriod updatedPeriod = evaluationPeriodRepository.save(period);
        log.info("Evaluation period updated successfully with ID: {}", updatedPeriod.getId());
        
        return convertToDTO(updatedPeriod);
    }

    @Override
    @Transactional
    public void deleteEvaluationPeriod(Long id) {
        log.info("Deleting evaluation period with ID: {}", id);
        
        if (!evaluationPeriodRepository.existsById(id)) {
            throw new RuntimeException("Evaluation period not found with ID: " + id);
        }
        
        evaluationPeriodRepository.deleteById(id);
        log.info("Evaluation period deleted successfully with ID: {}", id);
    }

    @Override
    public EvaluationPeriodDTO getEvaluationPeriodById(Long id) {
        log.info("Fetching evaluation period with ID: {}", id);
        EvaluationPeriod period = evaluationPeriodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation period not found with ID: " + id));
        return convertToDTO(period);
    }

    @Override
    public EvaluationPeriodDetailDTO getEvaluationPeriodDetailById(Long id) {
        log.info("Fetching evaluation period details with ID: {}", id);
        EvaluationPeriod period = evaluationPeriodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation period not found with ID: " + id));
        return convertToDetailDTO(period);
    }

    @Override
    public List<EvaluationPeriodDTO> getAllEvaluationPeriods() {
        log.info("Fetching all evaluation periods");
        return evaluationPeriodRepository.findAllByOrderByStartDateAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationPeriodDTO> getEvaluationPeriodsByStatus(String status) {
        log.info("Fetching evaluation periods with status: {}", status);
        return evaluationPeriodRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationPeriodDTO> getEvaluationPeriodsByAcademicYearAndSemester(String academicYear, String semester) {
        log.info("Fetching evaluation periods for {} - {}", academicYear, semester);
        return evaluationPeriodRepository.findByAcademicYearAndSemester(academicYear, semester)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationPeriodDTO> getActiveEvaluationPeriods() {
        log.info("Fetching active evaluation periods");
        return evaluationPeriodRepository.findByStatusOrderByStartDateAsc("active")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationPeriodDTO> getUpcomingEvaluationPeriods() {
        log.info("Fetching upcoming evaluation periods");
        return evaluationPeriodRepository.findUpcomingPeriods(LocalDate.now())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationPeriodDTO> getPastEvaluationPeriods() {
        log.info("Fetching past evaluation periods");
        return evaluationPeriodRepository.findPastPeriods(LocalDate.now())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EvaluationPeriodDTO updateEvaluationPeriodStatus(Long id, EvaluationPeriodStatusUpdateDTO statusUpdateDTO) {
        log.info("Updating status for evaluation period with ID: {} to {}", id, statusUpdateDTO.getStatus());
        
        EvaluationPeriod period = evaluationPeriodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation period not found with ID: " + id));
        
        period.setStatus(statusUpdateDTO.getStatus());
        EvaluationPeriod updatedPeriod = evaluationPeriodRepository.save(period);
        log.info("Status updated successfully for period ID: {}", updatedPeriod.getId());
        
        return convertToDTO(updatedPeriod);
    }

    @Override
    public boolean existsEvaluationPeriod(Long id) {
        return evaluationPeriodRepository.existsById(id);
    }

    @Override
    public long getCountByStatus(String status) {
        return evaluationPeriodRepository.countByStatus(status);
    }

    @Override
    public boolean isPeriodActive(Long id) {
        EvaluationPeriod period = evaluationPeriodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation period not found with ID: " + id));
        
        LocalDate now = LocalDate.now();
        return "active".equals(period.getStatus()) && 
               !now.isBefore(period.getStartDate()) && 
               !now.isAfter(period.getEndDate());
    }

    @Override
    public boolean hasActivePeriodForSemester(String academicYear, String semester) {
        return evaluationPeriodRepository.existsActivePeriodByAcademicYearAndSemester(academicYear, semester);
    }

    /**
     * Convert Entity to DTO
     */
    private EvaluationPeriodDTO convertToDTO(EvaluationPeriod period) {
        return EvaluationPeriodDTO.builder()
                .id(period.getId())
                .title(period.getTitle())
                .academicYear(period.getAcademicYear())
                .semester(period.getSemester())
                .startDate(period.getStartDate())
                .endDate(period.getEndDate())
                .status(period.getStatus())
                .createdAt(period.getCreatedAt())
                .updatedAt(period.getUpdatedAt())
                .build();
    }

    /**
     * Convert Entity to Detail DTO
     */
    private EvaluationPeriodDetailDTO convertToDetailDTO(EvaluationPeriod period) {
        LocalDate now = LocalDate.now();
        long daysRemaining = ChronoUnit.DAYS.between(now, period.getEndDate());
        boolean isActive = "active".equals(period.getStatus()) && 
                          !now.isBefore(period.getStartDate()) && 
                          !now.isAfter(period.getEndDate());
        boolean isUpcoming = period.getStartDate().isAfter(now);
        boolean isPast = period.getEndDate().isBefore(now);
        
        return EvaluationPeriodDetailDTO.builder()
                .id(period.getId())
                .title(period.getTitle())
                .academicYear(period.getAcademicYear())
                .semester(period.getSemester())
                .startDate(period.getStartDate())
                .endDate(period.getEndDate())
                .status(period.getStatus())
                .daysRemaining(daysRemaining)
                .isActive(isActive)
                .isUpcoming(isUpcoming)
                .isPast(isPast)
                .createdAt(period.getCreatedAt())
                .updatedAt(period.getUpdatedAt())
                .build();
    }
}