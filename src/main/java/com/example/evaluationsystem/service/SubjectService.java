// src/main/java/com/example/evaluationsystem/service/SubjectService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.SubjectDTO;
import com.example.evaluationsystem.dto.SubjectDetailDTO;
import com.example.evaluationsystem.dto.SubjectRequestDTO;

import java.util.List;

public interface SubjectService {
    SubjectDTO createSubject(SubjectRequestDTO requestDTO);
    SubjectDTO updateSubject(Long id, SubjectRequestDTO requestDTO);
    void deleteSubject(Long id);
    SubjectDTO getSubjectById(Long id);
    SubjectDetailDTO getSubjectDetailById(Long id);
    List<SubjectDTO> getAllSubjects();
    List<SubjectDTO> getSubjectsByDepartment(Integer departmentId);
    List<SubjectDTO> searchSubjects(String searchTerm);
    long getSubjectCountByDepartment(Integer departmentId);
    boolean existsSubject(Long id);
    boolean existsSubjectByCode(String subjectCode);
}