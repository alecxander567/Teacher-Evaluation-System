// src/main/java/com/example/evaluationsystem/repository/DepartmentRepository.java
package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    // Find department by name (case insensitive)
    Optional<Department> findByNameIgnoreCase(String name);
    
    // Check if department exists by name (for validation)
    boolean existsByNameIgnoreCase(String name);
    
    // Search departments by name (case insensitive)
    List<Department> findByNameContainingIgnoreCase(String name);
    
    // Get departments with teacher count
    @Query("SELECT d, COUNT(t.id) as teacherCount " +
           "FROM Department d " +
           "LEFT JOIN Teacher t ON t.departmentId = d.id " +
           "GROUP BY d.id")
    List<Object[]> findDepartmentsWithTeacherCount();
    
    // Find department with its teachers
    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.teachers WHERE d.id = :id")
    Optional<Department> findByIdWithTeachers(@Param("id") Integer id);
    
    // Get departments with at least one teacher
    @Query("SELECT DISTINCT d FROM Department d JOIN Teacher t ON t.departmentId = d.id")
    List<Department> findDepartmentsWithTeachers();
    
    // Get departments with no teachers
    @Query("SELECT d FROM Department d WHERE d.id NOT IN " +
           "(SELECT DISTINCT t.departmentId FROM Teacher t WHERE t.departmentId IS NOT NULL)")
    List<Department> findDepartmentsWithoutTeachers();
}