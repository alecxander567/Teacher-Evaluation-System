// src/main/java/com/example/evaluationsystem/service/impl/TeacherAssignmentServiceImpl.java
package com.example.evaluationsystem.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.evaluationsystem.dto.TeacherAssignmentDTO;
import com.example.evaluationsystem.dto.TeacherSelectionDTO;
import com.example.evaluationsystem.model.Teacher;
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

    @Override
    public List<TeacherSelectionDTO> getTeacherSelectionList(
            String academicYear, String semester, Integer departmentId) {

        List<TeacherAssignment> assignments =
                teacherAssignmentRepository.findByAcademicYearAndSemesterWithDetails(academicYear, semester);

        // Group by teacherId, preserving first-seen order. The first assignment
        // found per teacher becomes the "representative" assignment used to
        // satisfy teacherAssignmentId on submission - since we evaluate the
        // teacher once regardless of how many subjects they teach.
        Map<Long, List<TeacherAssignment>> byTeacher = assignments.stream()
                .collect(Collectors.groupingBy(
                        TeacherAssignment::getTeacherId, LinkedHashMap::new, Collectors.toList()));

        List<TeacherSelectionDTO> result = new ArrayList<>();

        for (List<TeacherAssignment> teacherAssignments : byTeacher.values()) {
            TeacherAssignment representative = teacherAssignments.get(0);
            Teacher teacher = representative.getTeacher();

            if (departmentId != null
                    && (teacher.getDepartmentId() == null || !teacher.getDepartmentId().equals(departmentId))) {
                continue;
            }

            String departmentName = teacher.getDepartment() != null ? teacher.getDepartment().getName() : null;

            result.add(TeacherSelectionDTO.builder()
                    .teacherId(teacher.getId())
                    .firstName(teacher.getFirstName())
                    .lastName(teacher.getLastName())
                    .fullName(teacher.getFullName())
                    .employmentType(teacher.getEmploymentType() != null ? teacher.getEmploymentType().name() : null)
                    .departmentId(teacher.getDepartmentId())
                    .departmentName(departmentName)
                    .teacherAssignmentId(representative.getId())
                    .hasMultipleAssignments(teacherAssignments.size() > 1)
                    .build());
        }

        result.sort(Comparator.comparing(TeacherSelectionDTO::getLastName, Comparator.nullsLast(String::compareTo)));
        return result;
    }

    @Override
    public TeacherAssignmentDTO createAssignment(TeacherAssignmentDTO request) {

        boolean alreadyExists = teacherAssignmentRepository
                .existsByTeacherIdAndSubjectIdAndAcademicYearAndSemester(
                        request.getTeacherId(),
                        request.getSubjectId(),
                        request.getAcademicYear(),
                        request.getSemester());

        if (alreadyExists) {
            throw new RuntimeException(
                    "This teacher is already assigned to this subject for the given academic year and semester.");
        }

        TeacherAssignment ta = new TeacherAssignment();
        ta.setTeacherId(request.getTeacherId());
        ta.setSubjectId(request.getSubjectId());
        ta.setAcademicYear(request.getAcademicYear());
        ta.setSemester(request.getSemester());

        TeacherAssignment saved = teacherAssignmentRepository.save(ta);

        // teacher/subject are insertable=false/updatable=false read-only associations,
        // so they won't be populated on `saved` yet - reload to get them joined in
        // for convertToDTO to fill in teacherName/subjectName.
        TeacherAssignment reloaded = teacherAssignmentRepository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Failed to reload saved assignment"));

        return convertToDTO(reloaded);
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