// src/main/java/com/example/evaluationsystem/repository/EvaluationLinkRepository.java
package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.EvaluationLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationLinkRepository extends JpaRepository<EvaluationLink, Long> {

    Optional<EvaluationLink> findByToken(String token);

    List<EvaluationLink> findByEvaluationFormId(Long evaluationFormId);

    List<EvaluationLink> findByStatus(String status);

    @Query("SELECT el FROM EvaluationLink el JOIN FETCH el.evaluationForm ef JOIN FETCH ef.evaluationPeriod WHERE el.id = :id")
    Optional<EvaluationLink> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT el FROM EvaluationLink el JOIN FETCH el.evaluationForm ef JOIN FETCH ef.evaluationPeriod WHERE el.token = :token")
    Optional<EvaluationLink> findByTokenWithDetails(@Param("token") String token);

    boolean existsByToken(String token);
}