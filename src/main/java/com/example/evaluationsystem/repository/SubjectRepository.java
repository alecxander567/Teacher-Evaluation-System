// src/main/java/com/example/evaluationsystem/repository/SubjectRepository.java
package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // Find subject by subject code (case insensitive)
    Optional<Subject> findBySubjectCodeIgnoreCase(String subjectCode);
    
    // Check if subject code exists (for validation)
    boolean existsBySubjectCodeIgnoreCase(String subjectCode);
    
    // Find subjects by department ID
    List<Subject> findByDepartmentId(Integer departmentId);
    
    // Find subjects by department ID with sorting
    List<Subject> findByDepartmentIdOrderBySubjectNameAsc(Integer departmentId);
    
    // Search subjects by subject code or name (case insensitive)
    @Query("SELECT s FROM Subject s WHERE LOWER(s.subjectCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Subject> searchByCodeOrName(@Param("searchTerm") String searchTerm);
    
    // Get subjects with department names
    @Query("SELECT s, d.name FROM Subject s LEFT JOIN Department d ON s.departmentId = d.id")
    List<Object[]> findSubjectsWithDepartmentNames();
    
    // Find subject with department details
    @Query("SELECT s FROM Subject s LEFT JOIN FETCH s.department WHERE s.id = :id")
    Optional<Subject> findByIdWithDepartment(@Param("id") Long id);
    
    // Count subjects by department
    long countByDepartmentId(Integer departmentId);
    
    // Check if subject code exists excluding a specific ID (for update validation)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
           "FROM Subject s WHERE LOWER(s.subjectCode) = LOWER(:subjectCode) AND s.id != :id")
    boolean existsBySubjectCodeIgnoreCaseAndIdNot(@Param("subjectCode") String subjectCode, @Param("id") Long id);
}