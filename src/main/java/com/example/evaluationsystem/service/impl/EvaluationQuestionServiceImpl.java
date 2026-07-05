// src/main/java/com/example/evaluationsystem/service/impl/EvaluationQuestionServiceImpl.java
package com.example.evaluationsystem.service.impl;

import com.example.evaluationsystem.dto.EvaluationQuestionDTO;
import com.example.evaluationsystem.dto.EvaluationQuestionRequestDTO;
import com.example.evaluationsystem.model.EvaluationQuestion;
import com.example.evaluationsystem.repository.EvaluationCategoryRepository;
import com.example.evaluationsystem.repository.EvaluationQuestionRepository;
import com.example.evaluationsystem.service.EvaluationQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationQuestionServiceImpl implements EvaluationQuestionService {

    private final EvaluationQuestionRepository questionRepository;
    private final EvaluationCategoryRepository categoryRepository;

    @Override
    public EvaluationQuestionDTO createQuestion(EvaluationQuestionRequestDTO requestDTO) {
        // Validate category exists
        if (!categoryRepository.existsById(requestDTO.getCategoryId())) {
            throw new RuntimeException("Category not found with id: " + requestDTO.getCategoryId());
        }

        EvaluationQuestion question = new EvaluationQuestion();
        question.setCategoryId(requestDTO.getCategoryId());
        question.setQuestion(requestDTO.getQuestion());
        question.setDisplayOrder(requestDTO.getDisplayOrder());
        question.setIsRequired(requestDTO.getIsRequired());

        EvaluationQuestion savedQuestion = questionRepository.save(question);
        return convertToDTO(savedQuestion);
    }

    @Override
    public EvaluationQuestionDTO updateQuestion(Long id, EvaluationQuestionRequestDTO requestDTO) {
        EvaluationQuestion existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));

        // Validate category exists
        if (!categoryRepository.existsById(requestDTO.getCategoryId())) {
            throw new RuntimeException("Category not found with id: " + requestDTO.getCategoryId());
        }

        existingQuestion.setCategoryId(requestDTO.getCategoryId());
        existingQuestion.setQuestion(requestDTO.getQuestion());
        existingQuestion.setDisplayOrder(requestDTO.getDisplayOrder());
        existingQuestion.setIsRequired(requestDTO.getIsRequired());

        EvaluationQuestion updatedQuestion = questionRepository.save(existingQuestion);
        return convertToDTO(updatedQuestion);
    }

    @Override
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteQuestionsByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }
        questionRepository.deleteByCategoryId(categoryId);
    }

    @Override
    public EvaluationQuestionDTO getQuestionById(Long id) {
        EvaluationQuestion question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        return convertToDTO(question);
    }

    @Override
    public List<EvaluationQuestionDTO> getQuestionsByCategoryId(Long categoryId) {
        return questionRepository.findByCategoryIdOrderByDisplayOrderAsc(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationQuestionDTO> getQuestionsByFormId(Long formId) {
        return questionRepository.findQuestionsByFormId(formId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsQuestion(Long id) {
        return questionRepository.existsById(id);
    }

    @Override
    public long countQuestionsByCategoryId(Long categoryId) {
        return questionRepository.countByCategoryId(categoryId);
    }

    private EvaluationQuestionDTO convertToDTO(EvaluationQuestion question) {
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