// src/main/java/com/example/evaluationsystem/service/impl/EvaluationFormServiceImpl.java
package com.example.evaluationsystem.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.evaluationsystem.dto.EvaluationCategoryDTO;
import com.example.evaluationsystem.dto.EvaluationFormDTO;
import com.example.evaluationsystem.dto.EvaluationFormDetailDTO;
import com.example.evaluationsystem.dto.EvaluationFormRequestDTO;
import com.example.evaluationsystem.dto.EvaluationQuestionDTO;
import com.example.evaluationsystem.model.EvaluationCategory;
import com.example.evaluationsystem.model.EvaluationForm;
import com.example.evaluationsystem.model.EvaluationPeriod;
import com.example.evaluationsystem.model.EvaluationQuestion;
import com.example.evaluationsystem.repository.EvaluationCategoryRepository;
import com.example.evaluationsystem.repository.EvaluationFormRepository;
import com.example.evaluationsystem.repository.EvaluationPeriodRepository;
import com.example.evaluationsystem.repository.EvaluationQuestionRepository;
import com.example.evaluationsystem.service.EvaluationFormService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationFormServiceImpl implements EvaluationFormService {

    private final EvaluationFormRepository evaluationFormRepository;
    private final EvaluationPeriodRepository evaluationPeriodRepository;
    private final EvaluationCategoryRepository evaluationCategoryRepository;
    private final EvaluationQuestionRepository evaluationQuestionRepository;

    @Override
    public EvaluationFormDTO createEvaluationForm(EvaluationFormRequestDTO requestDTO) {
        // Validate that the evaluation period exists
        EvaluationPeriod period = evaluationPeriodRepository.findById(requestDTO.getEvaluationPeriodId())
                .orElseThrow(() -> new RuntimeException("Evaluation period not found with id: " + requestDTO.getEvaluationPeriodId()));

        // Check if period is active or in draft (you can adjust this logic)
        if (!period.getStatus().equals("active") && !period.getStatus().equals("draft")) {
            throw new RuntimeException("Cannot create form for period with status: " + period.getStatus());
        }

        EvaluationForm form = new EvaluationForm();
        form.setEvaluationPeriodId(requestDTO.getEvaluationPeriodId());
        form.setTitle(requestDTO.getTitle());
        form.setDescription(requestDTO.getDescription());

        EvaluationForm savedForm = evaluationFormRepository.save(form);
        return convertToDTO(savedForm);
    }

    @Override
    public EvaluationFormDTO updateEvaluationForm(Long id, EvaluationFormRequestDTO requestDTO) {
        EvaluationForm existingForm = evaluationFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation form not found with id: " + id));

        // Validate that the evaluation period exists
        EvaluationPeriod period = evaluationPeriodRepository.findById(requestDTO.getEvaluationPeriodId())
                .orElseThrow(() -> new RuntimeException("Evaluation period not found with id: " + requestDTO.getEvaluationPeriodId()));

        existingForm.setEvaluationPeriodId(requestDTO.getEvaluationPeriodId());
        existingForm.setTitle(requestDTO.getTitle());
        existingForm.setDescription(requestDTO.getDescription());

        EvaluationForm updatedForm = evaluationFormRepository.save(existingForm);
        return convertToDTO(updatedForm);
    }

    @Override
    public void deleteEvaluationForm(Long id) {
        if (!evaluationFormRepository.existsById(id)) {
            throw new RuntimeException("Evaluation form not found with id: " + id);
        }
        evaluationFormRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteFormsByEvaluationPeriod(Long evaluationPeriodId) {
        // Check if period exists
        if (!evaluationPeriodRepository.existsById(evaluationPeriodId)) {
            throw new RuntimeException("Evaluation period not found with id: " + evaluationPeriodId);
        }
        evaluationFormRepository.deleteByEvaluationPeriodId(evaluationPeriodId);
    }

    @Override
    public EvaluationFormDTO getEvaluationFormById(Long id) {
        EvaluationForm form = evaluationFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation form not found with id: " + id));
        return convertToDTO(form);
    }

    @Override
    public EvaluationFormDetailDTO getEvaluationFormDetailById(Long id) {
        EvaluationForm form = evaluationFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation form not found with id: " + id));
        return convertToDetailDTO(form);
    }

    @Override
    public List<EvaluationFormDTO> getAllEvaluationForms() {
        return evaluationFormRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationFormDTO> getFormsByEvaluationPeriodId(Long evaluationPeriodId) {
        // Check if period exists
        if (!evaluationPeriodRepository.existsById(evaluationPeriodId)) {
            throw new RuntimeException("Evaluation period not found with id: " + evaluationPeriodId);
        }
        return evaluationFormRepository.findByEvaluationPeriodId(evaluationPeriodId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationFormDTO> searchFormsByTitle(String title) {
        return evaluationFormRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationFormDTO> getFormsByPeriodStatus(String status) {
        return evaluationFormRepository.findByEvaluationPeriodStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsEvaluationForm(Long id) {
        return evaluationFormRepository.existsById(id);
    }

    @Override
    public boolean existsFormsForPeriod(Long evaluationPeriodId) {
        return evaluationFormRepository.existsByEvaluationPeriodId(evaluationPeriodId);
    }

    @Override
    public long countFormsByPeriodId(Long evaluationPeriodId) {
        return evaluationFormRepository.countByEvaluationPeriodId(evaluationPeriodId);
    }

    @Override
    public List<EvaluationFormDTO> getFormsWithPeriodDetails() {
        return evaluationFormRepository.findAllWithPeriods().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper methods for conversion
    private EvaluationFormDTO convertToDTO(EvaluationForm form) {
        return new EvaluationFormDTO(
                form.getId(),
                form.getEvaluationPeriodId(),
                form.getTitle(),
                form.getDescription(),
                form.getCreatedAt(),
                form.getUpdatedAt()
        );
    }

    private EvaluationFormDetailDTO convertToDetailDTO(EvaluationForm form) {
        EvaluationFormDetailDTO detailDTO = new EvaluationFormDetailDTO();
        detailDTO.setId(form.getId());
        detailDTO.setEvaluationPeriodId(form.getEvaluationPeriodId());
        detailDTO.setTitle(form.getTitle());
        detailDTO.setDescription(form.getDescription());
        detailDTO.setCreatedAt(form.getCreatedAt());
        detailDTO.setUpdatedAt(form.getUpdatedAt());

        // Get evaluation period details (if loaded)
        if (form.getEvaluationPeriod() != null) {
            EvaluationPeriod period = form.getEvaluationPeriod();
            detailDTO.setEvaluationPeriodTitle(period.getTitle());
            detailDTO.setEvaluationPeriodStatus(period.getStatus());

            // Check if period is active
            LocalDate today = LocalDate.now();
            boolean isActive = period.getStatus().equals("active")
                    && !today.isBefore(period.getStartDate())
                    && !today.isAfter(period.getEndDate());
            detailDTO.setPeriodActive(isActive);
        } else {
            // If period not loaded, fetch it separately
            EvaluationPeriod period = evaluationPeriodRepository.findById(form.getEvaluationPeriodId())
                    .orElse(null);
            if (period != null) {
                detailDTO.setEvaluationPeriodTitle(period.getTitle());
                detailDTO.setEvaluationPeriodStatus(period.getStatus());

                LocalDate today = LocalDate.now();
                boolean isActive = period.getStatus().equals("active")
                        && !today.isBefore(period.getStartDate())
                        && !today.isAfter(period.getEndDate());
                detailDTO.setPeriodActive(isActive);
            }
        }

        List<EvaluationCategory> categories = evaluationCategoryRepository.findByFormIdOrderByDisplayOrderAsc(form.getId());
        List<EvaluationCategoryDTO> categoryDTOs = categories.stream()
                .map(this::convertCategoryToDTO)
                .collect(Collectors.toList());
        detailDTO.setCategories(categoryDTOs);

        int totalQuestions = categoryDTOs.stream()
                .mapToInt(category -> category.getQuestions() == null ? 0 : category.getQuestions().size())
                .sum();
        detailDTO.setTotalQuestions(totalQuestions);
        detailDTO.setTotalResponses(0);

        return detailDTO;
    }

    private EvaluationCategoryDTO convertCategoryToDTO(EvaluationCategory category) {
        EvaluationCategoryDTO dto = new EvaluationCategoryDTO();
        dto.setId(category.getId());
        dto.setFormId(category.getFormId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setDisplayOrder(category.getDisplayOrder());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());

        List<EvaluationQuestion> questions = evaluationQuestionRepository.findByCategoryIdOrderByDisplayOrderAsc(category.getId());
        dto.setQuestions(questions.stream().map(this::convertQuestionToDTO).collect(Collectors.toList()));
        dto.setQuestionCount((long) questions.size());
        return dto;
    }

    private EvaluationQuestionDTO convertQuestionToDTO(EvaluationQuestion question) {
        EvaluationQuestionDTO dto = new EvaluationQuestionDTO();
        dto.setId(question.getId());
        dto.setCategoryId(question.getCategoryId());
        dto.setQuestion(question.getQuestion());
        dto.setDisplayOrder(question.getDisplayOrder());
        dto.setIsRequired(question.getIsRequired());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setUpdatedAt(question.getUpdatedAt());
        return dto;
    }
}
