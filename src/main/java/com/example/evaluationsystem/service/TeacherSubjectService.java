// src/main/java/com/example/evaluationsystem/service/TeacherSubjectService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.TeacherSubjectDTO;
import com.example.evaluationsystem.dto.TeacherSubjectDetailDTO;
import com.example.evaluationsystem.dto.TeacherSubjectRequestDTO;

import java.util.List;

public interface TeacherSubjectService {
    TeacherSubjectDTO assignTeacherToSubject(TeacherSubjectRequestDTO requestDTO);
    TeacherSubjectDTO updateAssignment(Long id, TeacherSubjectRequestDTO requestDTO);
    void removeAssignment(Long id);
    TeacherSubjectDTO getAssignmentById(Long id);
    TeacherSubjectDetailDTO getAssignmentDetailById(Long id);
    List<TeacherSubjectDTO> getAllAssignments(); 
    List<TeacherSubjectDTO> getAssignmentsByTeacher(Long teacherId);
    List<TeacherSubjectDTO> getAssignmentsBySubject(Long subjectId);
    List<TeacherSubjectDTO> getAssignmentsByAcademicYearAndSemester(String academicYear, String semester);
    List<TeacherSubjectDTO> getAssignmentsByTeacherAndSemester(Long teacherId, String academicYear, String semester);
    List<TeacherSubjectDTO> getAssignmentsBySubjectAndSemester(Long subjectId, String academicYear, String semester);
    long getAssignmentCountByTeacher(Long teacherId);
    long getAssignmentCountBySubject(Long subjectId);
    boolean existsAssignment(Long id);
    boolean isTeacherAssignedToSubject(Long teacherId, Long subjectId, String academicYear, String semester);
}