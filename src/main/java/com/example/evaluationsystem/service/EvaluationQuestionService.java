// src/main/java/com/example/evaluationsystem/service/EvaluationQuestionService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.EvaluationQuestionDTO;
import com.example.evaluationsystem.dto.EvaluationQuestionRequestDTO;

import java.util.List;

public interface EvaluationQuestionService {
    EvaluationQuestionDTO createQuestion(EvaluationQuestionRequestDTO requestDTO);
    EvaluationQuestionDTO updateQuestion(Long id, EvaluationQuestionRequestDTO requestDTO);
    void deleteQuestion(Long id);
    void deleteQuestionsByCategoryId(Long categoryId);
    EvaluationQuestionDTO getQuestionById(Long id);
    List<EvaluationQuestionDTO> getQuestionsByCategoryId(Long categoryId);
    List<EvaluationQuestionDTO> getQuestionsByFormId(Long formId);
    boolean existsQuestion(Long id);
    long countQuestionsByCategoryId(Long categoryId);
}