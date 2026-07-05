// src/main/java/com/example/evaluationsystem/service/impl/EvaluationCategoryServiceImpl.java
package com.example.evaluationsystem.service.impl;

import com.example.evaluationsystem.dto.EvaluationCategoryDTO;
import com.example.evaluationsystem.dto.EvaluationCategoryRequestDTO;
import com.example.evaluationsystem.dto.EvaluationQuestionDTO;
import com.example.evaluationsystem.dto.EvaluationQuestionRequestDTO;
import com.example.evaluationsystem.model.EvaluationCategory;
import com.example.evaluationsystem.model.EvaluationForm;
import com.example.evaluationsystem.repository.EvaluationCategoryRepository;
import com.example.evaluationsystem.repository.EvaluationFormRepository;
import com.example.evaluationsystem.service.EvaluationCategoryService;
import com.example.evaluationsystem.service.EvaluationQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationCategoryServiceImpl implements EvaluationCategoryService {

    private final EvaluationCategoryRepository categoryRepository;
    private final EvaluationFormRepository formRepository;
    private final EvaluationQuestionService questionService;

    @Override
    public EvaluationCategoryDTO createCategory(EvaluationCategoryRequestDTO requestDTO) {
        // Validate form exists
        if (!formRepository.existsById(requestDTO.getFormId())) {
            throw new RuntimeException("Evaluation form not found with id: " + requestDTO.getFormId());
        }

        EvaluationCategory category = new EvaluationCategory();
        category.setFormId(requestDTO.getFormId());
        category.setName(requestDTO.getName());
        category.setDescription(requestDTO.getDescription());
        category.setDisplayOrder(requestDTO.getDisplayOrder());

        EvaluationCategory savedCategory = categoryRepository.save(category);

        // Create questions if provided
        if (requestDTO.getQuestions() != null && !requestDTO.getQuestions().isEmpty()) {
            for (EvaluationQuestionRequestDTO questionRequest : requestDTO.getQuestions()) {
                questionRequest.setCategoryId(savedCategory.getId());
                questionService.createQuestion(questionRequest);
            }
        }

        return convertToDTO(savedCategory);
    }

    @Override
    public EvaluationCategoryDTO updateCategory(Long id, EvaluationCategoryRequestDTO requestDTO) {
        EvaluationCategory existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Validate form exists
        if (!formRepository.existsById(requestDTO.getFormId())) {
            throw new RuntimeException("Evaluation form not found with id: " + requestDTO.getFormId());
        }

        existingCategory.setFormId(requestDTO.getFormId());
        existingCategory.setName(requestDTO.getName());
        existingCategory.setDescription(requestDTO.getDescription());
        existingCategory.setDisplayOrder(requestDTO.getDisplayOrder());

        EvaluationCategory updatedCategory = categoryRepository.save(existingCategory);
        return convertToDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCategoriesByFormId(Long formId) {
        if (!formRepository.existsById(formId)) {
            throw new RuntimeException("Evaluation form not found with id: " + formId);
        }
        categoryRepository.deleteByFormId(formId);
    }

    @Override
    public EvaluationCategoryDTO getCategoryById(Long id) {
        EvaluationCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return convertToDTO(category);
    }

    @Override
    public List<EvaluationCategoryDTO> getCategoriesByFormId(Long formId) {
        return categoryRepository.findByFormIdOrderByDisplayOrderAsc(formId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationCategoryDTO> getCategoriesWithQuestionsByFormId(Long formId) {
        return categoryRepository.findCategoriesWithQuestionsByFormId(formId).stream()
                .map(this::convertToDTOWithQuestions)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsCategory(Long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public boolean existsCategoriesForForm(Long formId) {
        return categoryRepository.existsByFormId(formId);
    }

    @Override
    public long countCategoriesByFormId(Long formId) {
        return categoryRepository.countByFormId(formId);
    }

    private EvaluationCategoryDTO convertToDTO(EvaluationCategory category) {
        EvaluationCategoryDTO dto = new EvaluationCategoryDTO();
        dto.setId(category.getId());
        dto.setFormId(category.getFormId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setDisplayOrder(category.getDisplayOrder());
        dto.setQuestionCount(questionService.countQuestionsByCategoryId(category.getId()));
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }

    private EvaluationCategoryDTO convertToDTOWithQuestions(EvaluationCategory category) {
        EvaluationCategoryDTO dto = convertToDTO(category);
        if (category.getQuestions() != null) {
            dto.setQuestions(category.getQuestions().stream()
                    .map(question -> {
                        EvaluationQuestionDTO questionDTO = new EvaluationQuestionDTO();
                        questionDTO.setId(question.getId());
                        questionDTO.setCategoryId(question.getCategoryId());
                        questionDTO.setQuestion(question.getQuestion());
                        questionDTO.setDisplayOrder(question.getDisplayOrder());
                        questionDTO.setIsRequired(question.getIsRequired());
                        questionDTO.setCreatedAt(question.getCreatedAt());
                        questionDTO.setUpdatedAt(question.getUpdatedAt());
                        return questionDTO;
                    })
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}