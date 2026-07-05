// src/main/java/com/example/evaluationsystem/repository/EvaluationCategoryRepository.java
package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.EvaluationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationCategoryRepository extends JpaRepository<EvaluationCategory, Long> {

    List<EvaluationCategory> findByFormIdOrderByDisplayOrderAsc(Long formId);

    @Query("SELECT c FROM EvaluationCategory c LEFT JOIN FETCH c.questions WHERE c.formId = :formId ORDER BY c.displayOrder ASC")
    List<EvaluationCategory> findCategoriesWithQuestionsByFormId(@Param("formId") Long formId);

    long countByFormId(Long formId);

    void deleteByFormId(Long formId);

    boolean existsByFormId(Long formId);
}