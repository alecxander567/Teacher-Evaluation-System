package com.example.evaluationsystem.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.example.evaluationsystem.dto.EvaluationFormDetailDTO;
import com.example.evaluationsystem.model.EvaluationCategory;
import com.example.evaluationsystem.model.EvaluationForm;
import com.example.evaluationsystem.model.EvaluationPeriod;
import com.example.evaluationsystem.model.EvaluationQuestion;
import com.example.evaluationsystem.repository.EvaluationCategoryRepository;
import com.example.evaluationsystem.repository.EvaluationFormRepository;
import com.example.evaluationsystem.repository.EvaluationPeriodRepository;
import com.example.evaluationsystem.repository.EvaluationQuestionRepository;

class EvaluationFormServiceImplTest {

    @Test
    void getEvaluationFormDetailById_shouldIncludeCategoriesAndQuestions() {
        EvaluationFormRepository formRepository = Mockito.mock(EvaluationFormRepository.class);
        EvaluationPeriodRepository periodRepository = Mockito.mock(EvaluationPeriodRepository.class);
        EvaluationCategoryRepository categoryRepository = Mockito.mock(EvaluationCategoryRepository.class);
        EvaluationQuestionRepository questionRepository = Mockito.mock(EvaluationQuestionRepository.class);
        EvaluationFormServiceImpl service = new EvaluationFormServiceImpl(formRepository, periodRepository, categoryRepository, questionRepository);

        EvaluationForm form = new EvaluationForm();
        form.setId(1L);
        form.setEvaluationPeriodId(10L);
        form.setTitle("Sample Form");
        form.setDescription("Sample Description");

        EvaluationPeriod period = new EvaluationPeriod();
        period.setId(10L);
        period.setTitle("Spring 2026");
        period.setStatus("active");
        period.setStartDate(LocalDate.now().minusDays(1));
        period.setEndDate(LocalDate.now().plusDays(1));

        EvaluationCategory category = new EvaluationCategory();
        category.setId(100L);
        category.setFormId(1L);
        category.setName("Teaching Quality");

        EvaluationQuestion question = new EvaluationQuestion();
        question.setId(200L);
        question.setCategoryId(100L);
        question.setQuestion("How clear were the instructions?");
        question.setDisplayOrder(1);
        question.setIsRequired(true);

        when(formRepository.findById(1L)).thenReturn(Optional.of(form));
        when(periodRepository.findById(10L)).thenReturn(Optional.of(period));
        when(categoryRepository.findByFormIdOrderByDisplayOrderAsc(1L)).thenReturn(List.of(category));
        when(questionRepository.findByCategoryIdOrderByDisplayOrderAsc(100L)).thenReturn(List.of(question));

        EvaluationFormDetailDTO detail = service.getEvaluationFormDetailById(1L);

        assertThat(detail.getCategories()).isNotNull();
        assertThat(detail.getCategories()).hasSize(1);
        assertThat(detail.getCategories().get(0).getQuestions()).hasSize(1);
        assertThat(detail.getTotalQuestions()).isEqualTo(1);
    }
}
