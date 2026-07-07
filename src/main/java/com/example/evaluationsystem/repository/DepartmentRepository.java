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
    
    // Check if department name exists
    boolean existsByNameIgnoreCase(String name);
    
    // Search departments by name
    List<Department> findByNameContainingIgnoreCase(String name);
    
    // Get all departments with their teachers count
    @Query("SELECT d FROM Department d")
    List<Department> findAllWithTeacherCount();
    
    // Get departments that have at least one teacher
    @Query("SELECT DISTINCT d FROM Department d JOIN d.teachers t")
    List<Department> findDepartmentsWithTeachers();
    
    // Get departments without any teachers
    @Query("SELECT d FROM Department d WHERE d.id NOT IN (SELECT DISTINCT t.departmentId FROM Teacher t WHERE t.departmentId IS NOT NULL)")
    List<Department> findDepartmentsWithoutTeachers();
    
    // Find department by ID with teachers eagerly loaded
    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.teachers WHERE d.id = :id")
    Optional<Department> findByIdWithTeachers(@Param("id") Integer id);
}