// src/main/java/com/example/evaluationsystem/service/impl/SubjectServiceImpl.java
package com.example.evaluationsystem.service.impl;

import com.example.evaluationsystem.dto.SubjectDTO;
import com.example.evaluationsystem.dto.SubjectDetailDTO;
import com.example.evaluationsystem.dto.SubjectRequestDTO;
import com.example.evaluationsystem.model.Subject;
import com.example.evaluationsystem.repository.DepartmentRepository;
import com.example.evaluationsystem.repository.SubjectRepository;
import com.example.evaluationsystem.repository.TeacherRepository;
import com.example.evaluationsystem.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;

    @Override
    @Transactional
    public SubjectDTO createSubject(SubjectRequestDTO requestDTO) {
        log.info("Creating new subject with code: {}", requestDTO.getSubjectCode());
        
        // Check if subject code already exists
        if (subjectRepository.existsBySubjectCodeIgnoreCase(requestDTO.getSubjectCode())) {
            throw new RuntimeException("Subject with code '" + requestDTO.getSubjectCode() + "' already exists");
        }
        
        // Validate department if provided
        if (requestDTO.getDepartmentId() != null) {
            if (!departmentRepository.existsById(requestDTO.getDepartmentId())) {
                throw new RuntimeException("Department not found with ID: " + requestDTO.getDepartmentId());
            }
        }
        
        Subject subject = new Subject();
        subject.setDepartmentId(requestDTO.getDepartmentId());
        subject.setSubjectCode(requestDTO.getSubjectCode().toUpperCase());
        subject.setSubjectName(requestDTO.getSubjectName());
        subject.setDescription(requestDTO.getDescription());
        
        Subject savedSubject = subjectRepository.save(subject);
        log.info("Subject created successfully with ID: {}", savedSubject.getId());
        
        return convertToDTO(savedSubject);
    }

    @Override
    @Transactional
    public SubjectDTO updateSubject(Long id, SubjectRequestDTO requestDTO) {
        log.info("Updating subject with ID: {}", id);
        
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + id));
        
        // Check if subject code is being changed and if new code already exists
        if (!subject.getSubjectCode().equalsIgnoreCase(requestDTO.getSubjectCode()) &&
            subjectRepository.existsBySubjectCodeIgnoreCase(requestDTO.getSubjectCode())) {
            throw new RuntimeException("Subject with code '" + requestDTO.getSubjectCode() + "' already exists");
        }
        
        // Validate department if provided
        if (requestDTO.getDepartmentId() != null) {
            if (!departmentRepository.existsById(requestDTO.getDepartmentId())) {
                throw new RuntimeException("Department not found with ID: " + requestDTO.getDepartmentId());
            }
        }
        
        subject.setDepartmentId(requestDTO.getDepartmentId());
        subject.setSubjectCode(requestDTO.getSubjectCode().toUpperCase());
        subject.setSubjectName(requestDTO.getSubjectName());
        subject.setDescription(requestDTO.getDescription());
        
        Subject updatedSubject = subjectRepository.save(subject);
        log.info("Subject updated successfully with ID: {}", updatedSubject.getId());
        
        return convertToDTO(updatedSubject);
    }

    @Override
    @Transactional
    public void deleteSubject(Long id) {
        log.info("Deleting subject with ID: {}", id);
        
        if (!subjectRepository.existsById(id)) {
            throw new RuntimeException("Subject not found with ID: " + id);
        }
        
        subjectRepository.deleteById(id);
        log.info("Subject deleted successfully with ID: {}", id);
    }

    @Override
    public SubjectDTO getSubjectById(Long id) {
        log.info("Fetching subject with ID: {}", id);
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + id));
        return convertToDTO(subject);
    }

    @Override
    public SubjectDetailDTO getSubjectDetailById(Long id) {
        log.info("Fetching subject details with ID: {}", id);
        Subject subject = subjectRepository.findByIdWithDepartment(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + id));
        return convertToDetailDTO(subject);
    }

    @Override
    public List<SubjectDTO> getAllSubjects() {
        log.info("Fetching all subjects");
        return subjectRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDTO> getSubjectsByDepartment(Integer departmentId) {
        log.info("Fetching subjects for department ID: {}", departmentId);
        
        if (!departmentRepository.existsById(departmentId)) {
            throw new RuntimeException("Department not found with ID: " + departmentId);
        }
        
        return subjectRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDTO> searchSubjects(String searchTerm) {
        log.info("Searching subjects with term: {}", searchTerm);
        return subjectRepository.searchByCodeOrName(searchTerm)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getSubjectCountByDepartment(Integer departmentId) {
        return subjectRepository.countByDepartmentId(departmentId);
    }

    @Override
    public boolean existsSubject(Long id) {
        return subjectRepository.existsById(id);
    }

    @Override
    public boolean existsSubjectByCode(String subjectCode) {
        return subjectRepository.existsBySubjectCodeIgnoreCase(subjectCode);
    }

    /**
     * Convert Entity to DTO
     */
    private SubjectDTO convertToDTO(Subject subject) {
        String departmentName = null;
        if (subject.getDepartmentId() != null) {
            // Fetch department name directly without lambda
            departmentRepository.findById(subject.getDepartmentId())
                    .ifPresent(dept -> {
                        // We need to use a different approach since departmentName is not effectively final
                    });
            // Better approach - use a local variable
            var deptOptional = departmentRepository.findById(subject.getDepartmentId());
            if (deptOptional.isPresent()) {
                departmentName = deptOptional.get().getName();
            }
        }
        
        return SubjectDTO.builder()
                .id(subject.getId())
                .departmentId(subject.getDepartmentId())
                .departmentName(departmentName)
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .description(subject.getDescription())
                .createdAt(subject.getCreatedAt())
                .updatedAt(subject.getUpdatedAt())
                .build();
    }

    /**
     * Convert Entity to Detail DTO
     */
    private SubjectDetailDTO convertToDetailDTO(Subject subject) {
        String departmentName = null;
        long teacherCount = 0;
        
        if (subject.getDepartmentId() != null) {
            // Use a local variable approach
            var deptOptional = departmentRepository.findById(subject.getDepartmentId());
            if (deptOptional.isPresent()) {
                departmentName = deptOptional.get().getName();
            }
            teacherCount = teacherRepository.countByDepartmentId(subject.getDepartmentId());
        }
        
        return SubjectDetailDTO.builder()
                .id(subject.getId())
                .departmentId(subject.getDepartmentId())
                .departmentName(departmentName)
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .description(subject.getDescription())
                .teacherCount(teacherCount)
                .createdAt(subject.getCreatedAt())
                .updatedAt(subject.getUpdatedAt())
                .build();
    }
}