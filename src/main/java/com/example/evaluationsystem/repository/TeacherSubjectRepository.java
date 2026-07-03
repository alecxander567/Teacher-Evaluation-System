// src/main/java/com/example/evaluationsystem/repository/TeacherSubjectRepository.java
package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {

    // Find assignments by teacher ID
    List<TeacherSubject> findByTeacherId(Long teacherId);
    
    // Find assignments by teacher ID with subject details
    @Query("SELECT ts FROM TeacherSubject ts LEFT JOIN FETCH ts.subject WHERE ts.teacherId = :teacherId")
    List<TeacherSubject> findByTeacherIdWithSubject(@Param("teacherId") Long teacherId);
    
    // Find assignments by subject ID
    List<TeacherSubject> findBySubjectId(Long subjectId);
    
    // Find assignments by subject ID with teacher details
    @Query("SELECT ts FROM TeacherSubject ts LEFT JOIN FETCH ts.teacher WHERE ts.subjectId = :subjectId")
    List<TeacherSubject> findBySubjectIdWithTeacher(@Param("subjectId") Long subjectId);
    
    // Find assignments by academic year and semester
    List<TeacherSubject> findByAcademicYearAndSemester(String academicYear, String semester);
    
    // Check if assignment exists (for validation)
    boolean existsByTeacherIdAndSubjectIdAndAcademicYearAndSemester(
        Long teacherId, Long subjectId, String academicYear, String semester
    );
    
    // Check if assignment exists excluding a specific ID (for update validation)
    @Query("SELECT CASE WHEN COUNT(ts) > 0 THEN true ELSE false END " +
           "FROM TeacherSubject ts " +
           "WHERE ts.teacherId = :teacherId " +
           "AND ts.subjectId = :subjectId " +
           "AND ts.academicYear = :academicYear " +
           "AND ts.semester = :semester " +
           "AND ts.id != :id")
    boolean existsByTeacherIdAndSubjectIdAndAcademicYearAndSemesterAndIdNot(
        @Param("teacherId") Long teacherId,
        @Param("subjectId") Long subjectId,
        @Param("academicYear") String academicYear,
        @Param("semester") String semester,
        @Param("id") Long id
    );
    
    // Find assignments by teacher ID, academic year, and semester
    List<TeacherSubject> findByTeacherIdAndAcademicYearAndSemester(
        Long teacherId, String academicYear, String semester
    );
    
    // Find assignments by subject ID, academic year, and semester
    List<TeacherSubject> findBySubjectIdAndAcademicYearAndSemester(
        Long subjectId, String academicYear, String semester
    );
    
    // Delete all assignments for a teacher
    void deleteByTeacherId(Long teacherId);
    
    // Delete all assignments for a subject
    void deleteBySubjectId(Long subjectId);
    
    // Get count of assignments by teacher
    long countByTeacherId(Long teacherId);
    
    // Get count of assignments by subject
    long countBySubjectId(Long subjectId);
}