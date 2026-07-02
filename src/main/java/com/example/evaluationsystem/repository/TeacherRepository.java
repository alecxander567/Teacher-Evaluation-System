package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Find teacher by email (for validation)
    Optional<Teacher> findByEmail(String email);
    
    // Check if email exists (for validation)
    boolean existsByEmail(String email);
    
    // Find teachers by department ID
    List<Teacher> findByDepartmentId(Integer departmentId);
    
    // Find teachers by department ID with sorting
    List<Teacher> findByDepartmentIdOrderByLastNameAsc(Integer departmentId);
    
    // Search teachers by first name or last name (case insensitive)
    @Query("SELECT t FROM Teacher t WHERE LOWER(t.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Teacher> searchByName(@Param("searchTerm") String searchTerm);
    
    // Find teachers with pagination by department
    @Query("SELECT t FROM Teacher t WHERE t.departmentId = :departmentId")
    List<Teacher> findTeachersByDepartmentId(@Param("departmentId") Integer departmentId);
    
    // Count teachers by department
    long countByDepartmentId(Integer departmentId);
}