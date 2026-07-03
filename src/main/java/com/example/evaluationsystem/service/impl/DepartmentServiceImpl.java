// src/main/java/com/example/evaluationsystem/service/impl/DepartmentServiceImpl.java
package com.example.evaluationsystem.service.impl;

import com.example.evaluationsystem.dto.DepartmentDTO;
import com.example.evaluationsystem.dto.DepartmentDetailDTO;
import com.example.evaluationsystem.dto.DepartmentRequestDTO;
import com.example.evaluationsystem.dto.TeacherDTO;
import com.example.evaluationsystem.model.Department;
import com.example.evaluationsystem.model.Teacher;
import com.example.evaluationsystem.repository.DepartmentRepository;
import com.example.evaluationsystem.repository.TeacherRepository;
import com.example.evaluationsystem.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;

    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentRequestDTO requestDTO) {
        log.info("Creating new department: {}", requestDTO.getName());
        
        // Check if department name already exists
        if (departmentRepository.existsByNameIgnoreCase(requestDTO.getName())) {
            throw new RuntimeException("Department with name '" + requestDTO.getName() + "' already exists");
        }
        
        Department department = new Department();
        department.setName(requestDTO.getName());
        department.setDescription(requestDTO.getDescription());
        
        Department savedDepartment = departmentRepository.save(department);
        log.info("Department created successfully with ID: {}", savedDepartment.getId());
        
        return convertToDTO(savedDepartment);
    }

    @Override
    @Transactional
    public DepartmentDTO updateDepartment(Integer id, DepartmentRequestDTO requestDTO) {
        log.info("Updating department with ID: {}", id);
        
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));
        
        // Check if name is being changed and if new name already exists
        if (!department.getName().equalsIgnoreCase(requestDTO.getName()) && 
            departmentRepository.existsByNameIgnoreCase(requestDTO.getName())) {
            throw new RuntimeException("Department with name '" + requestDTO.getName() + "' already exists");
        }
        
        department.setName(requestDTO.getName());
        department.setDescription(requestDTO.getDescription());
        
        Department updatedDepartment = departmentRepository.save(department);
        log.info("Department updated successfully with ID: {}", updatedDepartment.getId());
        
        return convertToDTO(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(Integer id) {
        log.info("Deleting department with ID: {}", id);
        
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with ID: " + id);
        }
        
        // Check if department has teachers
        long teacherCount = teacherRepository.countByDepartmentId(id);
        if (teacherCount > 0) {
            throw new RuntimeException("Cannot delete department with " + teacherCount + 
                                     " teachers assigned. Please reassign or delete teachers first.");
        }
        
        departmentRepository.deleteById(id);
        log.info("Department deleted successfully with ID: {}", id);
    }

    @Override
    public DepartmentDTO getDepartmentById(Integer id) {
        log.info("Fetching department with ID: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));
        return convertToDTO(department);
    }

    @Override
    public DepartmentDetailDTO getDepartmentDetailById(Integer id) {
        log.info("Fetching department details with ID: {}", id);
        Department department = departmentRepository.findByIdWithTeachers(id)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));
        return convertToDetailDTO(department);
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        log.info("Fetching all departments");
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentDTO> searchDepartments(String searchTerm) {
        log.info("Searching departments with term: {}", searchTerm);
        return departmentRepository.findByNameContainingIgnoreCase(searchTerm)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentDTO> getDepartmentsWithTeachers() {
        log.info("Fetching departments with teachers");
        return departmentRepository.findDepartmentsWithTeachers()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentDTO> getDepartmentsWithoutTeachers() {
        log.info("Fetching departments without teachers");
        return departmentRepository.findDepartmentsWithoutTeachers()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getTeacherCountByDepartment(Integer departmentId) {
        return teacherRepository.countByDepartmentId(departmentId);
    }

    @Override
    public boolean existsDepartment(Integer id) {
        return departmentRepository.existsById(id);
    }

    @Override
    public boolean existsDepartmentByName(String name) {
        return departmentRepository.existsByNameIgnoreCase(name);
    }

    /**
     * Convert Entity to DTO
     */
    private DepartmentDTO convertToDTO(Department department) {
        long teacherCount = teacherRepository.countByDepartmentId(department.getId());
        
        return DepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .teacherCount((int) teacherCount)
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }

    /**
     * Convert Entity to Detail DTO with teachers
     */
    private DepartmentDetailDTO convertToDetailDTO(Department department) {
        List<TeacherDTO> teacherDTOs = department.getTeachers().stream()
                .map(this::convertTeacherToDTO)
                .collect(Collectors.toList());
        
        return DepartmentDetailDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .teacherCount(teacherDTOs.size())
                .teachers(teacherDTOs)
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }

    /**
     * Convert Teacher to TeacherDTO
     */
    private TeacherDTO convertTeacherToDTO(Teacher teacher) {
        return TeacherDTO.builder()
                .id(teacher.getId())
                .departmentId(teacher.getDepartmentId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .fullName(teacher.getFullName())
                .email(teacher.getEmail())
                .position(teacher.getPosition())
                .createdAt(teacher.getCreatedAt())
                .updatedAt(teacher.getUpdatedAt())
                .build();
    }
}