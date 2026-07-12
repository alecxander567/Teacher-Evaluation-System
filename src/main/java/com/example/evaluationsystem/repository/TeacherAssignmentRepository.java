// TeacherAssignmentRepository.java
package com.example.evaluationsystem.repository;

import com.example.evaluationsystem.model.TeacherAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherAssignmentRepository extends JpaRepository<TeacherAssignment, Long> {

    @Query("SELECT ta FROM TeacherAssignment ta "
            + "JOIN FETCH ta.teacher t "
            + "JOIN FETCH ta.subject s "
            + "WHERE ta.academicYear = :academicYear AND ta.semester = :semester")
    List<TeacherAssignment> findByAcademicYearAndSemesterWithDetails(
            @Param("academicYear") String academicYear,
            @Param("semester") String semester);

    List<TeacherAssignment> findByTeacherId(Long teacherId);

    List<TeacherAssignment> findBySubjectId(Long subjectId);

    boolean existsByTeacherIdAndSubjectIdAndAcademicYearAndSemester(
            Long teacherId, Long subjectId, String academicYear, String semester);
}