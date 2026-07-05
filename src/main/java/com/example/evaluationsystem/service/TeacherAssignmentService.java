package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.TeacherAssignmentDTO;

import java.util.List;

public interface TeacherAssignmentService {

    List<TeacherAssignmentDTO> getAssignmentsByAcademicYearAndSemester(String academicYear, String semester);

    TeacherAssignmentDTO getAssignmentById(Long id);

    List<TeacherAssignmentDTO> getAllAssignments();
}
