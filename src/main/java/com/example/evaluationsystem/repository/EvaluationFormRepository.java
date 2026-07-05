// src/main/java/com/example/evaluationsystem/repository/EvaluationFormRepository.java
package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.EvaluationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationFormRepository extends JpaRepository<EvaluationForm, Long> {

    // Find forms by evaluation period ID
    List<EvaluationForm> findByEvaluationPeriodId(Long evaluationPeriodId);
    
    // Find forms by evaluation period ID ordered by creation date
    List<EvaluationForm> findByEvaluationPeriodIdOrderByCreatedAtDesc(Long evaluationPeriodId);
    
    // Find forms by title (search functionality)
    List<EvaluationForm> findByTitleContainingIgnoreCase(String title);
    
    // Count forms by evaluation period ID
    long countByEvaluationPeriodId(Long evaluationPeriodId);
    
    // Find forms with their evaluation periods loaded (for performance)
    @Query("SELECT f FROM EvaluationForm f JOIN FETCH f.evaluationPeriod WHERE f.evaluationPeriodId = :periodId")
    List<EvaluationForm> findFormsWithPeriodByPeriodId(@Param("periodId") Long periodId);
    
    // Find all forms with their evaluation periods loaded
    @Query("SELECT f FROM EvaluationForm f JOIN FETCH f.evaluationPeriod")
    List<EvaluationForm> findAllWithPeriods();
    
    // Find forms by evaluation period status
    @Query("SELECT f FROM EvaluationForm f WHERE f.evaluationPeriod.status = :status")
    List<EvaluationForm> findByEvaluationPeriodStatus(@Param("status") String status);
    
    // Check if a form exists for a specific evaluation period
    boolean existsByEvaluationPeriodId(Long evaluationPeriodId);
    
    // Delete all forms for a specific evaluation period
    void deleteByEvaluationPeriodId(Long evaluationPeriodId);
}