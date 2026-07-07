// src/main/java/com/example/evaluationsystem/service/impl/StudentEvaluationServiceImpl.java
package com.example.evaluationsystem.service.impl;

import com.example.evaluationsystem.dto.*;
import com.example.evaluationsystem.model.*;
import com.example.evaluationsystem.repository.*;
import com.example.evaluationsystem.service.EvaluationFormService;
import com.example.evaluationsystem.service.EvaluationSubmissionService;  // ADD THIS IMPORT
import com.example.evaluationsystem.service.StudentEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentEvaluationServiceImpl implements StudentEvaluationService {

    private final TeacherAssignmentRepository teacherAssignmentRepository;
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final EvaluationSubmissionRepository submissionRepository;
    private final EvaluationFormService evaluationFormService;
    private final EvaluationSubmissionService submissionService;  // Now resolves correctly
    private final EvaluationPeriodRepository periodRepository;

    // In-memory session store (for demo - use Redis or database in production)
    private final Map<String, StudentEvaluationSessionDTO> sessions = new ConcurrentHashMap<>();

    @Override
    public List<DepartmentTeacherGroupDTO> getAvailableTeachersForPeriod(Long periodId, Long formId, String studentEmail) {
        // Get the evaluation period
        EvaluationPeriod period = periodRepository.findById(periodId)
                .orElseThrow(() -> new RuntimeException("Evaluation period not found"));

        // Get all teacher assignments for this period
        List<TeacherAssignment> assignments = teacherAssignmentRepository
                .findByAcademicYearAndSemesterWithDetails(period.getAcademicYear(), period.getSemester());

        // Get already evaluated teachers by this student
        List<Long> evaluatedAssignmentIds = submissionRepository
                .findByStudentEmail(studentEmail)
                .stream()
                .filter(sub -> sub.getEvaluationPeriodId().equals(periodId))
                .map(EvaluationSubmission::getTeacherAssignmentId)
                .collect(Collectors.toList());

        // Group by department
        Map<Integer, List<TeacherSelectionDTO>> departmentTeacherMap = new HashMap<>();

        for (TeacherAssignment assignment : assignments) {
            // Get teacher with department info
            Teacher teacher = teacherRepository.findById(assignment.getTeacherId()).orElse(null);
            if (teacher == null) continue;

            // Check if already evaluated
            boolean alreadyEvaluated = evaluatedAssignmentIds.contains(assignment.getId());

            TeacherSelectionDTO dto = new TeacherSelectionDTO();
            dto.setTeacherId(teacher.getId());
            dto.setFullName(teacher.getFullName());
            dto.setEmail(teacher.getEmail());
            dto.setPosition(teacher.getPosition());
            dto.setEmploymentType(teacher.getEmploymentType() != null ? 
                    teacher.getEmploymentType().getDisplayName() : "Not Specified");
            
            // Get department info
            Department department = null;
            if (teacher.getDepartmentId() != null) {
                department = departmentRepository.findById(teacher.getDepartmentId()).orElse(null);
            }
            if (department != null) {
                dto.setDepartmentId(department.getId());
                dto.setDepartmentName(department.getName());
            } else {
                dto.setDepartmentId(-1);
                dto.setDepartmentName("No Department");
            }
            
            // Get subject info from the assignment
            Subject subject = assignment.getSubject();
            if (subject != null) {
                dto.setSubjectId(subject.getId());
                dto.setSubjectName(subject.getSubjectName());
            }
            
            dto.setTeacherAssignmentId(assignment.getId());
            dto.setSelected(!alreadyEvaluated);

            // Group by department
            Integer deptId = department != null ? department.getId() : -1;
            departmentTeacherMap.computeIfAbsent(deptId, k -> new ArrayList<>()).add(dto);
        }

        // Convert to DTOs
        List<DepartmentTeacherGroupDTO> result = new ArrayList<>();
        
        for (Map.Entry<Integer, List<TeacherSelectionDTO>> entry : departmentTeacherMap.entrySet()) {
            Integer deptId = entry.getKey();
            String deptName = "No Department";
            if (deptId != -1) {
                Department dept = departmentRepository.findById(deptId).orElse(null);
                if (dept != null) {
                    deptName = dept.getName();
                }
            }
            
            DepartmentTeacherGroupDTO group = new DepartmentTeacherGroupDTO();
            group.setDepartmentId(deptId);
            group.setDepartmentName(deptName);
            group.setTeachers(entry.getValue());
            result.add(group);
        }

        // Sort departments by name
        result.sort(Comparator.comparing(DepartmentTeacherGroupDTO::getDepartmentName));

        return result;
    }

    @Override
    @Transactional
    public StudentEvaluationSessionDTO startEvaluationSession(StartEvaluationSessionRequestDTO request) {
        // Validate that all selected assignments exist
        List<TeacherAssignment> assignments = teacherAssignmentRepository
                .findAllById(request.getTeacherAssignmentIds());
        
        if (assignments.size() != request.getTeacherAssignmentIds().size()) {
            throw new RuntimeException("Some selected teacher assignments are invalid");
        }

        // Check for existing submissions
        for (Long assignmentId : request.getTeacherAssignmentIds()) {
            boolean exists = submissionRepository.existsByEvaluationPeriodIdAndTeacherAssignmentIdAndStudentEmail(
                    request.getEvaluationPeriodId(),
                    assignmentId,
                    request.getStudentEmail()
            );
            if (exists) {
                throw new RuntimeException("Student has already evaluated one of the selected teachers");
            }
        }

        // Generate session ID
        String sessionId = UUID.randomUUID().toString();

        // Create session
        StudentEvaluationSessionDTO session = new StudentEvaluationSessionDTO();
        session.setSessionId(sessionId);
        session.setStudentEmail(request.getStudentEmail());
        session.setStudentName(request.getStudentName());
        session.setEvaluationPeriodId(request.getEvaluationPeriodId());
        session.setEvaluationFormId(request.getEvaluationFormId());
        session.setSelectedTeacherAssignmentIds(new ArrayList<>(request.getTeacherAssignmentIds()));
        session.setCurrentIndex(0);
        session.setCurrentTeacherAssignmentId(request.getTeacherAssignmentIds().get(0));
        session.setTotalTeachers(request.getTeacherAssignmentIds().size());
        session.setCompletedCount(0);
        session.setStatus("IN_PROGRESS");

        // Set period and form titles
        periodRepository.findById(request.getEvaluationPeriodId())
                .ifPresent(period -> session.setEvaluationPeriodTitle(period.getTitle()));

        try {
            EvaluationFormDTO form = evaluationFormService.getEvaluationFormById(request.getEvaluationFormId());
            if (form != null) {
                session.setEvaluationFormTitle(form.getTitle());
            }
        } catch (Exception e) {
            log.warn("Could not load evaluation form title: {}", e.getMessage());
        }

        // Store session
        sessions.put(sessionId, session);

        log.info("Started evaluation session {} for student {} with {} teachers", 
                sessionId, request.getStudentEmail(), request.getTeacherAssignmentIds().size());

        return session;
    }

    @Override
    public StudentEvaluationSessionDTO getEvaluationSession(String sessionId) {
        StudentEvaluationSessionDTO session = sessions.get(sessionId);
        if (session == null) {
            throw new RuntimeException("Evaluation session not found");
        }
        return session;
    }

    @Override
    public TeacherEvaluationProgressDTO getNextTeacherToEvaluate(String sessionId) {
        StudentEvaluationSessionDTO session = getEvaluationSession(sessionId);
        
        if (session.getStatus().equals("COMPLETED")) {
            throw new RuntimeException("Evaluation session is already completed");
        }

        if (session.getCurrentIndex() >= session.getSelectedTeacherAssignmentIds().size()) {
            throw new RuntimeException("All teachers have been evaluated");
        }

        Long assignmentId = session.getSelectedTeacherAssignmentIds()
                .get(session.getCurrentIndex());
        
        // Get teacher and subject info
        TeacherAssignment assignment = teacherAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Teacher assignment not found"));

        TeacherEvaluationProgressDTO progress = new TeacherEvaluationProgressDTO();
        progress.setTeacherAssignmentId(assignmentId);
        
        Teacher teacher = assignment.getTeacher();
        if (teacher != null) {
            progress.setTeacherName(teacher.getFullName());
        } else {
            progress.setTeacherName("Unknown Teacher");
        }
        
        Subject subject = assignment.getSubject();
        if (subject != null) {
            progress.setSubjectName(subject.getSubjectName());
        } else {
            progress.setSubjectName("Unknown Subject");
        }
        
        progress.setEvaluated(false);
        progress.setCurrentIndex(session.getCurrentIndex() + 1);
        progress.setTotalCount(session.getTotalTeachers());

        return progress;
    }

    @Override
    public List<TeacherEvaluationProgressDTO> getSessionProgress(String sessionId) {
        StudentEvaluationSessionDTO session = getEvaluationSession(sessionId);
        
        List<TeacherEvaluationProgressDTO> progress = new ArrayList<>();
        
        for (int i = 0; i < session.getSelectedTeacherAssignmentIds().size(); i++) {
            Long assignmentId = session.getSelectedTeacherAssignmentIds().get(i);
            boolean evaluated = i < session.getCompletedCount();
            
            TeacherAssignment assignment = teacherAssignmentRepository.findById(assignmentId)
                    .orElse(null);
            
            TeacherEvaluationProgressDTO dto = new TeacherEvaluationProgressDTO();
            dto.setTeacherAssignmentId(assignmentId);
            
            if (assignment != null) {
                Teacher teacher = assignment.getTeacher();
                dto.setTeacherName(teacher != null ? teacher.getFullName() : "Unknown Teacher");
                
                Subject subject = assignment.getSubject();
                dto.setSubjectName(subject != null ? subject.getSubjectName() : "Unknown Subject");
            } else {
                dto.setTeacherName("Unknown Teacher");
                dto.setSubjectName("Unknown Subject");
            }
            
            dto.setEvaluated(evaluated);
            dto.setCurrentIndex(i + 1);
            dto.setTotalCount(session.getTotalTeachers());
            
            progress.add(dto);
        }
        
        return progress;
    }

    @Override
    public boolean isSessionComplete(String sessionId) {
        StudentEvaluationSessionDTO session = getEvaluationSession(sessionId);
        return session.getStatus().equals("COMPLETED") || 
               session.getCompletedCount() >= session.getTotalTeachers();
    }

    @Override
    public EvaluationFormDetailDTO getTeacherEvaluationForm(String sessionId, Long teacherAssignmentId) {
        StudentEvaluationSessionDTO session = getEvaluationSession(sessionId);
        
        // Verify the teacher is in the session and is the current one
        if (!session.getSelectedTeacherAssignmentIds().contains(teacherAssignmentId)) {
            throw new RuntimeException("Teacher not in current evaluation session");
        }
        
        // Verify this is the current teacher
        Long currentId = session.getCurrentTeacherAssignmentId();
        if (!currentId.equals(teacherAssignmentId)) {
            throw new RuntimeException("Please complete the current teacher evaluation first");
        }

        // Get the evaluation form
        return evaluationFormService.getEvaluationFormDetailById(session.getEvaluationFormId());
    }

    @Override
    @Transactional
    public TeacherEvaluationProgressDTO submitTeacherEvaluation(
            String sessionId, 
            EvaluationSubmissionRequestDTO submissionRequest) {
        
        StudentEvaluationSessionDTO session = getEvaluationSession(sessionId);
        
        if (session.getStatus().equals("COMPLETED")) {
            throw new RuntimeException("Evaluation session is already completed");
        }

        // Verify the submission is for the current teacher
        Long currentAssignmentId = session.getCurrentTeacherAssignmentId();
        if (!currentAssignmentId.equals(submissionRequest.getTeacherAssignmentId())) {
            throw new RuntimeException("Can only submit evaluation for the current teacher");
        }

        // Check if already submitted
        boolean alreadySubmitted = submissionRepository.existsByEvaluationPeriodIdAndTeacherAssignmentIdAndStudentEmail(
                submissionRequest.getEvaluationPeriodId(),
                submissionRequest.getTeacherAssignmentId(),
                submissionRequest.getStudentEmail()
        );
        
        if (alreadySubmitted) {
            throw new RuntimeException("Evaluation for this teacher has already been submitted");
        }

        // Submit the evaluation
        submissionService.createSubmission(submissionRequest);

        // Update session progress
        session.setCompletedCount(session.getCompletedCount() + 1);
        
        // Move to next teacher or complete
        int nextIndex = session.getCurrentIndex() + 1;
        if (nextIndex < session.getSelectedTeacherAssignmentIds().size()) {
            session.setCurrentIndex(nextIndex);
            session.setCurrentTeacherAssignmentId(
                    session.getSelectedTeacherAssignmentIds().get(nextIndex)
            );
        } else {
            session.setStatus("COMPLETED");
            session.setCurrentTeacherAssignmentId(null);
        }

        log.info("Student {} completed evaluation for teacher assignment {}, {}/{} completed",
                session.getStudentEmail(), 
                currentAssignmentId, 
                session.getCompletedCount(), 
                session.getTotalTeachers());

        // Return next teacher or completion status
        if (session.getStatus().equals("COMPLETED")) {
            TeacherEvaluationProgressDTO complete = new TeacherEvaluationProgressDTO();
            complete.setTeacherAssignmentId(null);
            complete.setTeacherName(null);
            complete.setSubjectName(null);
            complete.setEvaluated(true);
            complete.setCurrentIndex(session.getTotalTeachers());
            complete.setTotalCount(session.getTotalTeachers());
            return complete;
        } else {
            return getNextTeacherToEvaluate(sessionId);
        }
    }

    @Override
    public void completeEvaluationSession(String sessionId) {
        StudentEvaluationSessionDTO session = getEvaluationSession(sessionId);
        session.setStatus("COMPLETED");
        session.setCurrentTeacherAssignmentId(null);
        log.info("Evaluation session {} completed", sessionId);
    }

    @Override
    public void cancelEvaluationSession(String sessionId) {
        StudentEvaluationSessionDTO session = sessions.remove(sessionId);
        if (session != null) {
            log.info("Evaluation session {} cancelled by student {}", sessionId, session.getStudentEmail());
        }
    }
}