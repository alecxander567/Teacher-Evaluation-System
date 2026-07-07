// src/main/java/com/example/evaluationsystem/service/TeacherAssignmentService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.TeacherAssignmentDTO;
import com.example.evaluationsystem.dto.TeacherSelectionDTO;

import java.util.List;

public interface TeacherAssignmentService {

    List<TeacherAssignmentDTO> getAssignmentsByAcademicYearAndSemester(String academicYear, String semester);

    TeacherAssignmentDTO getAssignmentById(Long id);

    List<TeacherAssignmentDTO> getAllAssignments();

    List<TeacherSelectionDTO> getTeacherSelectionList(String academicYear, String semester, Integer departmentId);
}