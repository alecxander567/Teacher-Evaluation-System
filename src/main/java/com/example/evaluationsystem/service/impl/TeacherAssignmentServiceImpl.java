package com.example.evaluationsystem.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.evaluationsystem.dto.TeacherAssignmentDTO;
import com.example.evaluationsystem.model.TeacherAssignment;
import com.example.evaluationsystem.repository.TeacherAssignmentRepository;
import com.example.evaluationsystem.service.TeacherAssignmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {

    private final TeacherAssignmentRepository teacherAssignmentRepository;

    @Override
    public List<TeacherAssignmentDTO> getAssignmentsByAcademicYearAndSemester(String academicYear, String semester) {
        return teacherAssignmentRepository
                .findByAcademicYearAndSemesterWithDetails(academicYear, semester)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherAssignmentDTO getAssignmentById(Long id) {
        TeacherAssignment ta = teacherAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher assignment not found with id: " + id));
        return convertToDTO(ta);
    }

    @Override
    public List<TeacherAssignmentDTO> getAllAssignments() {
        return teacherAssignmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TeacherAssignmentDTO convertToDTO(TeacherAssignment ta) {
        TeacherAssignmentDTO dto = new TeacherAssignmentDTO();
        dto.setId(ta.getId());
        dto.setTeacherId(ta.getTeacherId());
        dto.setSubjectId(ta.getSubjectId());
        dto.setAcademicYear(ta.getAcademicYear());
        dto.setSemester(ta.getSemester());

        if (ta.getTeacher() != null) {
            dto.setTeacherName(ta.getTeacher().getFullName());
        }
        if (ta.getSubject() != null) {
            dto.setSubjectName(ta.getSubject().getSubjectName());
        }

        return dto;
    }
}
