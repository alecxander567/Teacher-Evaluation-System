// src/main/java/com/example/evaluationsystem/model/EvaluationSubmission.java
package com.example.evaluationsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evaluation_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Evaluation period ID is required")
    @Column(name = "evaluation_period_id", nullable = false)
    private Long evaluationPeriodId;

    @NotNull(message = "Teacher assignment ID is required")
    @Column(name = "teacher_assignment_id", nullable = false)
    private Long teacherAssignmentId;

    @Column(name = "evaluation_link_id")
    private Long evaluationLinkId;

    @Column(name = "student_name", length = 255)
    private String studentName;

    @Email(message = "Invalid email format")
    @Column(name = "student_email", nullable = false, length = 255)
    private String studentEmail;

    @Column(name = "overall_comment", columnDefinition = "TEXT")
    private String overallComment;

    @Column(length = 20)
    private String status = "submitted";

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false)
    private LocalDateTime submittedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_period_id", insertable = false, updatable = false)
    private EvaluationPeriod evaluationPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_assignment_id", insertable = false, updatable = false)
    private TeacherAssignment teacherAssignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_link_id", insertable = false, updatable = false)
    private EvaluationLink evaluationLink;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluationResponse> responses = new ArrayList<>();
}