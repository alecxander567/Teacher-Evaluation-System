package com.example.evaluationsystem.service.impl;

import com.example.evaluationsystem.dto.EvaluationSubmissionDTO;
import com.example.evaluationsystem.model.EvaluationSubmission;
import com.example.evaluationsystem.model.Subject;
import com.example.evaluationsystem.model.Teacher;
import com.example.evaluationsystem.model.TeacherAssignment;
import com.example.evaluationsystem.repository.EvaluationPeriodRepository;
import com.example.evaluationsystem.repository.EvaluationResponseRepository;
import com.example.evaluationsystem.repository.EvaluationSubmissionRepository;
import com.example.evaluationsystem.repository.TeacherAssignmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluationSubmissionServiceImplTest {

    @Mock
    private EvaluationSubmissionRepository submissionRepository;

    @Mock
    private EvaluationResponseRepository responseRepository;

    @Mock
    private EvaluationPeriodRepository periodRepository;

    @Mock
    private TeacherAssignmentRepository assignmentRepository;

    @InjectMocks
    private EvaluationSubmissionServiceImpl service;

    @Test
    void getSubmissionById_shouldPopulateTeacherAndSubjectNames() {
        EvaluationSubmission submission = new EvaluationSubmission();
        submission.setId(1L);
        submission.setEvaluationPeriodId(2L);
        submission.setTeacherAssignmentId(3L);
        submission.setStudentEmail("student@example.com");

        TeacherAssignment assignment = new TeacherAssignment();
        assignment.setId(3L);

        Teacher teacher = new Teacher();
        teacher.setFirstName("Jane");
        teacher.setLastName("Doe");
        assignment.setTeacher(teacher);

        Subject subject = new Subject();
        subject.setSubjectName("Mathematics");
        assignment.setSubject(subject);

        submission.setTeacherAssignment(assignment);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));

        EvaluationSubmissionDTO dto = service.getSubmissionById(1L);

        assertEquals("Jane Doe", dto.getTeacherName());
        assertEquals("Mathematics", dto.getSubjectName());
    }
}
