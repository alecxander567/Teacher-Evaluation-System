// src/main/java/com/example/evaluationsystem/repository/EvaluationPeriodRepository.java
package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.EvaluationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EvaluationPeriodRepository extends JpaRepository<EvaluationPeriod, Long> {

    // Find periods by status
    List<EvaluationPeriod> findByStatus(String status);
    
    // Find periods by academic year and semester
    List<EvaluationPeriod> findByAcademicYearAndSemester(String academicYear, String semester);
    
    // Find active periods (status = 'active')
    List<EvaluationPeriod> findByStatusOrderByStartDateAsc(String status);
    
    // Find periods that are currently active
    @Query("SELECT ep FROM EvaluationPeriod ep WHERE ep.status = 'active' AND ep.startDate <= :currentDate AND ep.endDate >= :currentDate")
    List<EvaluationPeriod> findActivePeriodsAtDate(@Param("currentDate") LocalDate currentDate);
    
    // Find upcoming periods
    @Query("SELECT ep FROM EvaluationPeriod ep WHERE ep.startDate > :currentDate ORDER BY ep.startDate ASC")
    List<EvaluationPeriod> findUpcomingPeriods(@Param("currentDate") LocalDate currentDate);
    
    // Find past periods
    @Query("SELECT ep FROM EvaluationPeriod ep WHERE ep.endDate < :currentDate ORDER BY ep.endDate DESC")
    List<EvaluationPeriod> findPastPeriods(@Param("currentDate") LocalDate currentDate);
    
    // Check if any active period exists for a given academic year and semester
    @Query("SELECT CASE WHEN COUNT(ep) > 0 THEN true ELSE false END " +
           "FROM EvaluationPeriod ep " +
           "WHERE ep.academicYear = :academicYear " +
           "AND ep.semester = :semester " +
           "AND ep.status = 'active'")
    boolean existsActivePeriodByAcademicYearAndSemester(
        @Param("academicYear") String academicYear, 
        @Param("semester") String semester
    );
    
    // Count periods by status
    long countByStatus(String status);
    
    // Get all periods ordered by start date
    List<EvaluationPeriod> findAllByOrderByStartDateAsc();
}