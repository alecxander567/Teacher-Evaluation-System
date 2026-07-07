// src/main/java/com/example/evaluationsystem/service/impl/EvaluationSubmissionServiceImpl.java
package com.example.evaluationsystem.service.impl;

import com.example.evaluationsystem.dto.EvaluationBatchStatusRequestDTO;
import com.example.evaluationsystem.dto.EvaluationResponseDTO;
import com.example.evaluationsystem.dto.EvaluationResponseRequestDTO;
import com.example.evaluationsystem.dto.EvaluationSubmissionDTO;
import com.example.evaluationsystem.dto.EvaluationSubmissionRequestDTO;
import com.example.evaluationsystem.dto.TeacherEvaluationStatusDTO;
import com.example.evaluationsystem.model.EvaluationResponse;
import com.example.evaluationsystem.model.EvaluationSubmission;
import com.example.evaluationsystem.model.TeacherAssignment;
import com.example.evaluationsystem.repository.EvaluationPeriodRepository;
import com.example.evaluationsystem.repository.EvaluationResponseRepository;
import com.example.evaluationsystem.repository.EvaluationSubmissionRepository;
import com.example.evaluationsystem.repository.TeacherAssignmentRepository;
import com.example.evaluationsystem.service.EvaluationSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationSubmissionServiceImpl implements EvaluationSubmissionService {

    private final EvaluationSubmissionRepository submissionRepository;
    private final EvaluationResponseRepository responseRepository;
    private final EvaluationPeriodRepository periodRepository;
    private final TeacherAssignmentRepository assignmentRepository;

    @Override
    public EvaluationSubmissionDTO createSubmission(EvaluationSubmissionRequestDTO requestDTO) {
        // Validate period exists
        if (!periodRepository.existsById(requestDTO.getEvaluationPeriodId())) {
            throw new RuntimeException("Evaluation period not found with id: " + requestDTO.getEvaluationPeriodId());
        }

        // Validate assignment exists
        if (!assignmentRepository.existsById(requestDTO.getTeacherAssignmentId())) {
            throw new RuntimeException("Teacher assignment not found with id: " + requestDTO.getTeacherAssignmentId());
        }

        // Check if student already submitted
        if (hasStudentSubmitted(
                requestDTO.getEvaluationPeriodId(),
                requestDTO.getTeacherAssignmentId(),
                requestDTO.getStudentEmail())) {
            throw new RuntimeException("Student has already submitted evaluation for this period and teacher");
        }

        // Create submission
        EvaluationSubmission submission = new EvaluationSubmission();
        submission.setEvaluationPeriodId(requestDTO.getEvaluationPeriodId());
        submission.setTeacherAssignmentId(requestDTO.getTeacherAssignmentId());
        submission.setEvaluationLinkId(requestDTO.getEvaluationLinkId());
        submission.setStudentEmail(requestDTO.getStudentEmail());
        submission.setOverallComment(requestDTO.getOverallComment());

        EvaluationSubmission saved = submissionRepository.save(submission);

        // Save responses
        if (requestDTO.getResponses() != null && !requestDTO.getResponses().isEmpty()) {
            for (EvaluationResponseRequestDTO responseRequest : requestDTO.getResponses()) {
                EvaluationResponse response = new EvaluationResponse();
                response.setSubmissionId(saved.getId());
                response.setQuestionId(responseRequest.getQuestionId());
                response.setRating(responseRequest.getRating());
                responseRepository.save(response);
            }
        }

        return getSubmissionWithResponses(saved.getId());
    }

    @Override
    public EvaluationSubmissionDTO getSubmissionById(Long id) {
        EvaluationSubmission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + id));
        return convertToDTO(submission);
    }

    @Override
    public EvaluationSubmissionDTO getSubmissionWithResponses(Long id) {
        EvaluationSubmission submission = submissionRepository.findByIdWithResponses(id)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + id));
        return convertToDTOWithResponses(submission);
    }

    @Override
    public List<EvaluationSubmissionDTO> getSubmissionsByPeriodId(Long periodId) {
        return submissionRepository.findByEvaluationPeriodId(periodId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationSubmissionDTO> getSubmissionsByAssignmentId(Long assignmentId) {
        return submissionRepository.findByTeacherAssignmentId(assignmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationSubmissionDTO> getSubmissionsByStudentEmail(String email) {
        return submissionRepository.findByStudentEmail(email).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasStudentSubmitted(Long periodId, Long assignmentId, String studentEmail) {
        return submissionRepository.existsByEvaluationPeriodIdAndTeacherAssignmentIdAndStudentEmail(
                periodId, assignmentId, studentEmail);
    }

    @Override
    public long countSubmissionsByPeriodId(Long periodId) {
        return submissionRepository.countByEvaluationPeriodId(periodId);
    }

    @Override
    public long countSubmissionsByAssignmentId(Long assignmentId) {
        return submissionRepository.countByTeacherAssignmentId(assignmentId);
    }

    @Override
    public void deleteSubmission(Long id) {
        if (!submissionRepository.existsById(id)) {
            throw new RuntimeException("Submission not found with id: " + id);
        }
        // Delete associated responses first
        responseRepository.deleteBySubmissionId(id);
        submissionRepository.deleteById(id);
    }

    @Override
    public List<TeacherEvaluationStatusDTO> checkBatchSubmissionStatus(EvaluationBatchStatusRequestDTO requestDTO) {
        List<TeacherEvaluationStatusDTO> result = new ArrayList<>();

        for (Long assignmentId : requestDTO.getTeacherAssignmentIds()) {
            TeacherAssignment assignment = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Teacher assignment not found with id: " + assignmentId));

            boolean evaluated = submissionRepository.existsByEvaluationPeriodIdAndTeacherAssignmentIdAndStudentEmail(
                    requestDTO.getEvaluationPeriodId(), assignmentId, requestDTO.getStudentEmail());

            Long submissionId = null;
            if (evaluated) {
                submissionId = submissionRepository
                        .findByPeriodIdAndAssignmentId(requestDTO.getEvaluationPeriodId(), assignmentId)
                        .stream()
                        .filter(s -> requestDTO.getStudentEmail().equalsIgnoreCase(s.getStudentEmail()))
                        .map(EvaluationSubmission::getId)
                        .findFirst()
                        .orElse(null);
            }

            result.add(TeacherEvaluationStatusDTO.builder()
                    .teacherId(assignment.getTeacherId())
                    .teacherAssignmentId(assignmentId)
                    .teacherName(assignment.getTeacher() != null ? assignment.getTeacher().getFullName() : null)
                    .evaluated(evaluated)
                    .submissionId(submissionId)
                    .build());
        }

        return result;
    }

    private EvaluationSubmissionDTO convertToDTO(EvaluationSubmission submission) {
        EvaluationSubmissionDTO dto = new EvaluationSubmissionDTO();
        dto.setId(submission.getId());
        dto.setEvaluationPeriodId(submission.getEvaluationPeriodId());
        dto.setTeacherAssignmentId(submission.getTeacherAssignmentId());
        dto.setEvaluationLinkId(submission.getEvaluationLinkId());
        dto.setStudentEmail(submission.getStudentEmail());
        dto.setOverallComment(submission.getOverallComment());
        dto.setSubmittedAt(submission.getSubmittedAt());

        if (submission.getEvaluationPeriod() != null) {
            dto.setEvaluationPeriodTitle(submission.getEvaluationPeriod().getTitle());
        }

        if (submission.getTeacherAssignment() != null) {
            if (submission.getTeacherAssignment().getTeacher() != null) {
                dto.setTeacherName(submission.getTeacherAssignment().getTeacher().getFullName());
            }
            if (submission.getTeacherAssignment().getSubject() != null) {
                dto.setSubjectName(submission.getTeacherAssignment().getSubject().getSubjectName());
            }
        }

        return dto;
    }

    private EvaluationSubmissionDTO convertToDTOWithResponses(EvaluationSubmission submission) {
        EvaluationSubmissionDTO dto = convertToDTO(submission);

        if (submission.getResponses() != null && !submission.getResponses().isEmpty()) {
            List<EvaluationResponseDTO> responseDTOs = submission.getResponses().stream()
                    .map(response -> {
                        EvaluationResponseDTO responseDTO = new EvaluationResponseDTO();
                        responseDTO.setId(response.getId());
                        responseDTO.setSubmissionId(response.getSubmissionId());
                        responseDTO.setQuestionId(response.getQuestionId());
                        responseDTO.setRating(response.getRating());
                        responseDTO.setCreatedAt(response.getCreatedAt());

                        if (response.getQuestion() != null) {
                            responseDTO.setQuestionText(response.getQuestion().getQuestion());
                        }

                        return responseDTO;
                    })
                    .collect(Collectors.toList());

            dto.setResponses(responseDTOs);
            dto.setTotalQuestions(responseDTOs.size());

            long answered = responseDTOs.stream()
                    .filter(r -> r.getRating() != null)
                    .count();
            dto.setAnsweredQuestions((int) answered);

            double avg = responseDTOs.stream()
                    .filter(r -> r.getRating() != null)
                    .mapToInt(EvaluationResponseDTO::getRating)
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(avg);
        }

        return dto;
    }
}