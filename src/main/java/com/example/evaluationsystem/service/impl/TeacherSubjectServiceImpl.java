// src/main/java/com/example/evaluationsystem/service/impl/TeacherSubjectServiceImpl.java
package com.example.evaluationsystem.service.impl;

import com.example.evaluationsystem.dto.TeacherSubjectDTO;
import com.example.evaluationsystem.dto.TeacherSubjectDetailDTO;
import com.example.evaluationsystem.dto.TeacherSubjectRequestDTO;
import com.example.evaluationsystem.model.Subject;
import com.example.evaluationsystem.model.Teacher;
import com.example.evaluationsystem.model.TeacherSubject;
import com.example.evaluationsystem.repository.SubjectRepository;
import com.example.evaluationsystem.repository.TeacherRepository;
import com.example.evaluationsystem.repository.TeacherSubjectRepository;
import com.example.evaluationsystem.service.TeacherSubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherSubjectServiceImpl implements TeacherSubjectService {

    private final TeacherSubjectRepository teacherSubjectRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;

    @Override
    @Transactional
    public TeacherSubjectDTO assignTeacherToSubject(TeacherSubjectRequestDTO requestDTO) {
        log.info("Assigning teacher {} to subject {} for {} - {}", 
            requestDTO.getTeacherId(), requestDTO.getSubjectId(), 
            requestDTO.getAcademicYear(), requestDTO.getSemester());
        
        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(requestDTO.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + requestDTO.getTeacherId()));
        
        // Validate subject exists
        Subject subject = subjectRepository.findById(requestDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + requestDTO.getSubjectId()));
        
        // Check if assignment already exists
        if (teacherSubjectRepository.existsByTeacherIdAndSubjectIdAndAcademicYearAndSemester(
                requestDTO.getTeacherId(), requestDTO.getSubjectId(),
                requestDTO.getAcademicYear(), requestDTO.getSemester())) {
            throw new RuntimeException(
                "Teacher is already assigned to this subject for the specified academic year and semester"
            );
        }
        
        TeacherSubject assignment = new TeacherSubject();
        assignment.setTeacherId(requestDTO.getTeacherId());
        assignment.setSubjectId(requestDTO.getSubjectId());
        assignment.setAcademicYear(requestDTO.getAcademicYear());
        assignment.setSemester(requestDTO.getSemester());
        
        TeacherSubject savedAssignment = teacherSubjectRepository.save(assignment);
        log.info("Teacher assigned to subject successfully with ID: {}", savedAssignment.getId());
        
        return convertToDTO(savedAssignment, teacher, subject);
    }

    @Override
    @Transactional
    public TeacherSubjectDTO updateAssignment(Long id, TeacherSubjectRequestDTO requestDTO) {
        log.info("Updating assignment with ID: {}", id);
        
        TeacherSubject assignment = teacherSubjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + id));
        
        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(requestDTO.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + requestDTO.getTeacherId()));
        
        // Validate subject exists
        Subject subject = subjectRepository.findById(requestDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + requestDTO.getSubjectId()));
        
        // Check if assignment already exists (excluding current)
        if (teacherSubjectRepository.existsByTeacherIdAndSubjectIdAndAcademicYearAndSemesterAndIdNot(
                requestDTO.getTeacherId(), requestDTO.getSubjectId(),
                requestDTO.getAcademicYear(), requestDTO.getSemester(), id)) {
            throw new RuntimeException(
                "Another assignment already exists for this teacher, subject, academic year, and semester"
            );
        }
        
        assignment.setTeacherId(requestDTO.getTeacherId());
        assignment.setSubjectId(requestDTO.getSubjectId());
        assignment.setAcademicYear(requestDTO.getAcademicYear());
        assignment.setSemester(requestDTO.getSemester());
        
        TeacherSubject updatedAssignment = teacherSubjectRepository.save(assignment);
        log.info("Assignment updated successfully with ID: {}", updatedAssignment.getId());
        
        return convertToDTO(updatedAssignment, teacher, subject);
    }

    @Override
    @Transactional
    public void removeAssignment(Long id) {
        log.info("Removing assignment with ID: {}", id);
        
        if (!teacherSubjectRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found with ID: " + id);
        }
        
        teacherSubjectRepository.deleteById(id);
        log.info("Assignment removed successfully with ID: {}", id);
    }

    @Override
    public TeacherSubjectDTO getAssignmentById(Long id) {
        log.info("Fetching assignment with ID: {}", id);
        
        TeacherSubject assignment = teacherSubjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + id));
        
        Teacher teacher = teacherRepository.findById(assignment.getTeacherId()).orElse(null);
        Subject subject = subjectRepository.findById(assignment.getSubjectId()).orElse(null);
        
        return convertToDTO(assignment, teacher, subject);
    }

    @Override
    public TeacherSubjectDetailDTO getAssignmentDetailById(Long id) {
        log.info("Fetching assignment details with ID: {}", id);
        
        TeacherSubject assignment = teacherSubjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + id));
        
        Teacher teacher = teacherRepository.findById(assignment.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + assignment.getTeacherId()));
        
        Subject subject = subjectRepository.findById(assignment.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + assignment.getSubjectId()));
        
        return convertToDetailDTO(assignment, teacher, subject);
    }

    // ADD THIS METHOD - Get all assignments
    @Override
    public List<TeacherSubjectDTO> getAllAssignments() {
        log.info("Fetching all assignments");
        return teacherSubjectRepository.findAll()
                .stream()
                .map(assignment -> {
                    Teacher teacher = teacherRepository.findById(assignment.getTeacherId()).orElse(null);
                    Subject subject = subjectRepository.findById(assignment.getSubjectId()).orElse(null);
                    return convertToDTO(assignment, teacher, subject);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherSubjectDTO> getAssignmentsByTeacher(Long teacherId) {
        log.info("Fetching assignments for teacher ID: {}", teacherId);
        
        if (!teacherRepository.existsById(teacherId)) {
            throw new RuntimeException("Teacher not found with ID: " + teacherId);
        }
        
        return teacherSubjectRepository.findByTeacherIdWithSubject(teacherId)
                .stream()
                .map(assignment -> {
                    Teacher teacher = teacherRepository.findById(assignment.getTeacherId()).orElse(null);
                    Subject subject = assignment.getSubject();
                    return convertToDTO(assignment, teacher, subject);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherSubjectDTO> getAssignmentsBySubject(Long subjectId) {
        log.info("Fetching assignments for subject ID: {}", subjectId);
        
        if (!subjectRepository.existsById(subjectId)) {
            throw new RuntimeException("Subject not found with ID: " + subjectId);
        }
        
        return teacherSubjectRepository.findBySubjectIdWithTeacher(subjectId)
                .stream()
                .map(assignment -> {
                    Teacher teacher = assignment.getTeacher();
                    Subject subject = subjectRepository.findById(assignment.getSubjectId()).orElse(null);
                    return convertToDTO(assignment, teacher, subject);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherSubjectDTO> getAssignmentsByAcademicYearAndSemester(String academicYear, String semester) {
        log.info("Fetching assignments for {} - {}", academicYear, semester);
        
        return teacherSubjectRepository.findByAcademicYearAndSemester(academicYear, semester)
                .stream()
                .map(assignment -> {
                    Teacher teacher = teacherRepository.findById(assignment.getTeacherId()).orElse(null);
                    Subject subject = subjectRepository.findById(assignment.getSubjectId()).orElse(null);
                    return convertToDTO(assignment, teacher, subject);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherSubjectDTO> getAssignmentsByTeacherAndSemester(Long teacherId, String academicYear, String semester) {
        log.info("Fetching assignments for teacher {} - {} - {}", teacherId, academicYear, semester);
        
        return teacherSubjectRepository.findByTeacherIdAndAcademicYearAndSemester(teacherId, academicYear, semester)
                .stream()
                .map(assignment -> {
                    Teacher teacher = teacherRepository.findById(assignment.getTeacherId()).orElse(null);
                    Subject subject = subjectRepository.findById(assignment.getSubjectId()).orElse(null);
                    return convertToDTO(assignment, teacher, subject);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherSubjectDTO> getAssignmentsBySubjectAndSemester(Long subjectId, String academicYear, String semester) {
        log.info("Fetching assignments for subject {} - {} - {}", subjectId, academicYear, semester);
        
        return teacherSubjectRepository.findBySubjectIdAndAcademicYearAndSemester(subjectId, academicYear, semester)
                .stream()
                .map(assignment -> {
                    Teacher teacher = teacherRepository.findById(assignment.getTeacherId()).orElse(null);
                    Subject subject = subjectRepository.findById(assignment.getSubjectId()).orElse(null);
                    return convertToDTO(assignment, teacher, subject);
                })
                .collect(Collectors.toList());
    }

    @Override
    public long getAssignmentCountByTeacher(Long teacherId) {
        return teacherSubjectRepository.countByTeacherId(teacherId);
    }

    @Override
    public long getAssignmentCountBySubject(Long subjectId) {
        return teacherSubjectRepository.countBySubjectId(subjectId);
    }

    @Override
    public boolean existsAssignment(Long id) {
        return teacherSubjectRepository.existsById(id);
    }

    @Override
    public boolean isTeacherAssignedToSubject(Long teacherId, Long subjectId, String academicYear, String semester) {
        return teacherSubjectRepository.existsByTeacherIdAndSubjectIdAndAcademicYearAndSemester(
            teacherId, subjectId, academicYear, semester
        );
    }

    /**
     * Convert Entity to DTO
     */
    private TeacherSubjectDTO convertToDTO(TeacherSubject assignment, Teacher teacher, Subject subject) {
        String teacherName = teacher != null ? teacher.getFullName() : null;
        String subjectCode = subject != null ? subject.getSubjectCode() : null;
        String subjectName = subject != null ? subject.getSubjectName() : null;
        
        return TeacherSubjectDTO.builder()
                .id(assignment.getId())
                .teacherId(assignment.getTeacherId())
                .teacherName(teacherName)
                .subjectId(assignment.getSubjectId())
                .subjectCode(subjectCode)
                .subjectName(subjectName)
                .academicYear(assignment.getAcademicYear())
                .semester(assignment.getSemester())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();
    }

    /**
     * Convert Entity to Detail DTO
     */
    private TeacherSubjectDetailDTO convertToDetailDTO(TeacherSubject assignment, Teacher teacher, Subject subject) {
        return TeacherSubjectDetailDTO.builder()
                .id(assignment.getId())
                .teacherId(assignment.getTeacherId())
                .teacherName(teacher.getFullName())
                .teacherEmail(teacher.getEmail())
                .subjectId(assignment.getSubjectId())
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .subjectDescription(subject.getDescription())
                .academicYear(assignment.getAcademicYear())
                .semester(assignment.getSemester())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();
    }
}