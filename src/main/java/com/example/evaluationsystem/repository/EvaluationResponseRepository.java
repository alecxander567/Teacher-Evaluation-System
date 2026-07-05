// src/main/java/com/example/evaluationsystem/repository/EvaluationResponseRepository.java
package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.EvaluationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationResponseRepository extends JpaRepository<EvaluationResponse, Long> {

    List<EvaluationResponse> findBySubmissionId(Long submissionId);

    List<EvaluationResponse> findByQuestionId(Long questionId);

    @Query("SELECT AVG(er.rating) FROM EvaluationResponse er WHERE er.questionId = :questionId")
    Double getAverageRatingForQuestion(@Param("questionId") Long questionId);

    @Query("SELECT er.questionId, AVG(er.rating) FROM EvaluationResponse er GROUP BY er.questionId")
    List<Object[]> getAverageRatingForAllQuestions();

    void deleteBySubmissionId(Long submissionId);
}