// src/main/java/com/example/evaluationsystem/repository/EvaluationQuestionRepository.java
package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.EvaluationQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationQuestionRepository extends JpaRepository<EvaluationQuestion, Long> {

    List<EvaluationQuestion> findByCategoryIdOrderByDisplayOrderAsc(Long categoryId);

    @Query("SELECT q FROM EvaluationQuestion q WHERE q.category.formId = :formId ORDER BY q.category.displayOrder ASC, q.displayOrder ASC")
    List<EvaluationQuestion> findQuestionsByFormId(@Param("formId") Long formId);

    long countByCategoryId(Long categoryId);

    void deleteByCategoryId(Long categoryId);
}