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

    // ========== ADD THESE NEW METHODS ==========

    /**
     * Find teachers by department ID with department eagerly loaded
     * This prevents N+1 query issues when accessing department data
     */
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.department WHERE t.departmentId = :departmentId")
    List<Teacher> findByDepartmentIdWithDepartment(@Param("departmentId") Integer departmentId);

    /**
     * Search teachers by first name, last name, or email (case insensitive)
     * More comprehensive search than searchByName
     */
    @Query("SELECT t FROM Teacher t WHERE " +
           "LOWER(t.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(t.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Teacher> searchTeachers(@Param("searchTerm") String searchTerm);

    /**
     * Find all teachers with their departments eagerly loaded
     * Useful for bulk operations where you need department info
     */
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.department")
    List<Teacher> findAllWithDepartment();

    /**
     * Find teachers by employment type
     */
    List<Teacher> findByEmploymentType(com.example.evaluationsystem.model.EmploymentType employmentType);

    /**
     * Find teachers by department ID and employment type
     */
    List<Teacher> findByDepartmentIdAndEmploymentType(Integer departmentId, 
                                                       com.example.evaluationsystem.model.EmploymentType employmentType);

    /**
     * Count teachers by employment type
     */
    long countByEmploymentType(com.example.evaluationsystem.model.EmploymentType employmentType);

    /**
     * Find teachers who don't have a department assigned
     */
    @Query("SELECT t FROM Teacher t WHERE t.departmentId IS NULL")
    List<Teacher> findTeachersWithoutDepartment();

    /**
     * Find teachers with their assignments for a specific academic year and semester
     * This is useful for getting teachers with their subject assignments
     */
    @Query("SELECT DISTINCT t FROM Teacher t " +
           "LEFT JOIN FETCH t.department d " +
           "LEFT JOIN FETCH t.teacherAssignments ta " +
           "WHERE ta.academicYear = :academicYear " +
           "AND ta.semester = :semester")
    List<Teacher> findTeachersWithAssignmentsByYearAndSemester(
            @Param("academicYear") String academicYear,
            @Param("semester") String semester);
}