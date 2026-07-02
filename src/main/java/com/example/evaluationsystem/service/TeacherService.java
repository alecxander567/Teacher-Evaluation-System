package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.TeacherDTO;
import com.example.evaluationsystem.dto.TeacherRequestDTO;
import com.example.evaluationsystem.model.Teacher;
import com.example.evaluationsystem.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService {

    private final TeacherRepository teacherRepository;

    /**
     * Create a new teacher
     */
    @Transactional
    public TeacherDTO createTeacher(TeacherRequestDTO requestDTO) {
        log.info("Creating new teacher with email: {}", requestDTO.getEmail());
        
        // Check if email already exists
        if (teacherRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Teacher with email " + requestDTO.getEmail() + " already exists");
        }
        
        // Convert DTO to entity
        Teacher teacher = new Teacher();
        teacher.setDepartmentId(requestDTO.getDepartmentId());
        teacher.setFirstName(requestDTO.getFirstName());
        teacher.setLastName(requestDTO.getLastName());
        teacher.setEmail(requestDTO.getEmail());
        teacher.setPosition(requestDTO.getPosition());
        
        // Save to database
        Teacher savedTeacher = teacherRepository.save(teacher);
        log.info("Teacher created successfully with ID: {}", savedTeacher.getId());
        
        return convertToDTO(savedTeacher);
    }

    /**
     * Get all teachers
     */
    public List<TeacherDTO> getAllTeachers() {
        log.info("Fetching all teachers");
        return teacherRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get teacher by ID
     */
    public TeacherDTO getTeacherById(Long id) {
        log.info("Fetching teacher with ID: {}", id);
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + id));
        return convertToDTO(teacher);
    }

    /**
     * Get teachers by department ID
     */
    public List<TeacherDTO> getTeachersByDepartment(Integer departmentId) {
        log.info("Fetching teachers for department ID: {}", departmentId);
        return teacherRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update teacher
     */
    @Transactional
    public TeacherDTO updateTeacher(Long id, TeacherRequestDTO requestDTO) {
        log.info("Updating teacher with ID: {}", id);
        
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + id));
        
        // Check if email is being changed and if new email already exists
        if (!teacher.getEmail().equals(requestDTO.getEmail()) && 
            teacherRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email " + requestDTO.getEmail() + " is already taken");
        }
        
        // Update fields
        teacher.setDepartmentId(requestDTO.getDepartmentId());
        teacher.setFirstName(requestDTO.getFirstName());
        teacher.setLastName(requestDTO.getLastName());
        teacher.setEmail(requestDTO.getEmail());
        teacher.setPosition(requestDTO.getPosition());
        
        Teacher updatedTeacher = teacherRepository.save(teacher);
        log.info("Teacher updated successfully with ID: {}", updatedTeacher.getId());
        
        return convertToDTO(updatedTeacher);
    }

    /**
     * Delete teacher by ID
     */
    @Transactional
    public void deleteTeacher(Long id) {
        log.info("Deleting teacher with ID: {}", id);
        
        if (!teacherRepository.existsById(id)) {
            throw new RuntimeException("Teacher not found with ID: " + id);
        }
        
        teacherRepository.deleteById(id);
        log.info("Teacher deleted successfully with ID: {}", id);
    }

    /**
     * Search teachers by name
     */
    public List<TeacherDTO> searchTeachers(String searchTerm) {
        log.info("Searching teachers with term: {}", searchTerm);
        return teacherRepository.searchByName(searchTerm)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get count of teachers in a department
     */
    public long getTeacherCountByDepartment(Integer departmentId) {
        return teacherRepository.countByDepartmentId(departmentId);
    }

    /**
     * Check if teacher exists
     */
    public boolean existsTeacher(Long id) {
        return teacherRepository.existsById(id);
    }

    /**
     * Convert Entity to DTO
     */
    private TeacherDTO convertToDTO(Teacher teacher) {
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

    /**
     * Get teacher entity by ID (for internal use)
     */
    public Teacher getTeacherEntityById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + id));
    }
}