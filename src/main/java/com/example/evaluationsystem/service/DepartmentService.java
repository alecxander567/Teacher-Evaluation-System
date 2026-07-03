// src/main/java/com/example/evaluationsystem/service/DepartmentService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.DepartmentDTO;
import com.example.evaluationsystem.dto.DepartmentDetailDTO;
import com.example.evaluationsystem.dto.DepartmentRequestDTO;
import com.example.evaluationsystem.model.Department;

import java.util.List;

public interface DepartmentService {
    DepartmentDTO createDepartment(DepartmentRequestDTO requestDTO);
    DepartmentDTO updateDepartment(Integer id, DepartmentRequestDTO requestDTO);
    void deleteDepartment(Integer id);
    DepartmentDTO getDepartmentById(Integer id);
    DepartmentDetailDTO getDepartmentDetailById(Integer id);
    List<DepartmentDTO> getAllDepartments();
    List<DepartmentDTO> searchDepartments(String searchTerm);
    List<DepartmentDTO> getDepartmentsWithTeachers();
    List<DepartmentDTO> getDepartmentsWithoutTeachers();
    long getTeacherCountByDepartment(Integer departmentId);
    boolean existsDepartment(Integer id);
    boolean existsDepartmentByName(String name);
}