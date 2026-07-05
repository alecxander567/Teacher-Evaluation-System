// src/main/java/com/example/evaluationsystem/model/EvaluationLink.java
package com.example.evaluationsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluation_links")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Token is required")
    @Column(unique = true, nullable = false, length = 255)
    private String token;

    @NotNull(message = "Evaluation form ID is required")
    @Column(name = "evaluation_form_id", nullable = false)
    private Long evaluationFormId;

    @Column(length = 20)
    private String status = "active";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_form_id", insertable = false, updatable = false)
    private EvaluationForm evaluationForm;
}