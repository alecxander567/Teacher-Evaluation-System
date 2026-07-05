// src/main/java/com/example/evaluationsystem/repository/EvaluationSubmissionRepository.java
package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.EvaluationSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationSubmissionRepository extends JpaRepository<EvaluationSubmission, Long> {

    List<EvaluationSubmission> findByEvaluationPeriodId(Long evaluationPeriodId);

    List<EvaluationSubmission> findByTeacherAssignmentId(Long teacherAssignmentId);

    List<EvaluationSubmission> findByStudentEmail(String studentEmail);

    // ADD THIS METHOD - it's used in EvaluationLinkServiceImpl
    List<EvaluationSubmission> findByEvaluationLinkId(Long evaluationLinkId);

    @Query("SELECT es FROM EvaluationSubmission es JOIN FETCH es.responses WHERE es.id = :id")
    Optional<EvaluationSubmission> findByIdWithResponses(@Param("id") Long id);

    @Query("SELECT es FROM EvaluationSubmission es WHERE es.evaluationPeriodId = :periodId AND es.teacherAssignmentId = :assignmentId")
    List<EvaluationSubmission> findByPeriodIdAndAssignmentId(
            @Param("periodId") Long periodId,
            @Param("assignmentId") Long assignmentId);

    boolean existsByEvaluationPeriodIdAndTeacherAssignmentIdAndStudentEmail(
            Long periodId, Long assignmentId, String studentEmail);

    long countByEvaluationPeriodId(Long periodId);

    long countByTeacherAssignmentId(Long assignmentId);
}