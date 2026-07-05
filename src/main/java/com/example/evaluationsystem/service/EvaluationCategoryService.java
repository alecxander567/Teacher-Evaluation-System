// src/main/java/com/example/evaluationsystem/service/EvaluationCategoryService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.EvaluationCategoryDTO;
import com.example.evaluationsystem.dto.EvaluationCategoryRequestDTO;

import java.util.List;

public interface EvaluationCategoryService {
    EvaluationCategoryDTO createCategory(EvaluationCategoryRequestDTO requestDTO);
    EvaluationCategoryDTO updateCategory(Long id, EvaluationCategoryRequestDTO requestDTO);
    void deleteCategory(Long id);
    void deleteCategoriesByFormId(Long formId);
    EvaluationCategoryDTO getCategoryById(Long id);
    List<EvaluationCategoryDTO> getCategoriesByFormId(Long formId);
    List<EvaluationCategoryDTO> getCategoriesWithQuestionsByFormId(Long formId);
    boolean existsCategory(Long id);
    boolean existsCategoriesForForm(Long formId);
    long countCategoriesByFormId(Long formId);
}