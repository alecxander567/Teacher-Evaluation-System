// src/main/java/com/example/evaluationsystem/service/impl/EvaluationLinkServiceImpl.java
package com.example.evaluationsystem.service.impl;

import com.example.evaluationsystem.dto.EvaluationLinkDTO;
import com.example.evaluationsystem.dto.EvaluationLinkRequestDTO;
import com.example.evaluationsystem.model.EvaluationForm;
import com.example.evaluationsystem.model.EvaluationLink;
import com.example.evaluationsystem.model.EvaluationSubmission;
import com.example.evaluationsystem.repository.EvaluationFormRepository;
import com.example.evaluationsystem.repository.EvaluationLinkRepository;
import com.example.evaluationsystem.repository.EvaluationSubmissionRepository;
import com.example.evaluationsystem.service.EvaluationLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationLinkServiceImpl implements EvaluationLinkService {

    private final EvaluationLinkRepository linkRepository;
    private final EvaluationFormRepository formRepository;
    private final EvaluationSubmissionRepository submissionRepository;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public EvaluationLinkDTO createLink(EvaluationLinkRequestDTO requestDTO) {
        // Validate form exists
        EvaluationForm form = formRepository.findById(requestDTO.getEvaluationFormId())
                .orElseThrow(() -> new RuntimeException("Evaluation form not found with id: " + requestDTO.getEvaluationFormId()));

        // Generate unique token
        String token;
        do {
            token = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        } while (linkRepository.existsByToken(token));

        EvaluationLink link = new EvaluationLink();
        link.setToken(token);
        link.setEvaluationFormId(requestDTO.getEvaluationFormId());
        link.setStatus("active");

        EvaluationLink saved = linkRepository.save(link);
        return convertToDTO(saved);
    }

    @Override
    public EvaluationLinkDTO getLinkById(Long id) {
        EvaluationLink link = linkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Link not found with id: " + id));
        return convertToDTO(link);
    }

    @Override
    public EvaluationLinkDTO getLinkByToken(String token) {
        EvaluationLink link = linkRepository.findByTokenWithDetails(token)
                .orElseThrow(() -> new RuntimeException("Link not found with token: " + token));
        return convertToDTO(link);
    }

    @Override
    public List<EvaluationLinkDTO> getLinksByFormId(Long formId) {
        return linkRepository.findByEvaluationFormId(formId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationLinkDTO> getAllLinks() {
        return linkRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deactivateLink(Long id) {
        EvaluationLink link = linkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Link not found with id: " + id));
        link.setStatus("inactive");
        linkRepository.save(link);
    }

    @Override
    public void deleteLink(Long id) {
        if (!linkRepository.existsById(id)) {
            throw new RuntimeException("Link not found with id: " + id);
        }
        linkRepository.deleteById(id);
    }

    @Override
    public boolean isValidToken(String token) {
        return linkRepository.findByToken(token)
                .map(link -> {
                    if (!"active".equals(link.getStatus())) {
                        return false;
                    }
                    return true;
                })
                .orElse(false);
    }

    private EvaluationLinkDTO convertToDTO(EvaluationLink link) {
        EvaluationLinkDTO dto = new EvaluationLinkDTO();
        dto.setId(link.getId());
        dto.setToken(link.getToken());
        dto.setEvaluationFormId(link.getEvaluationFormId());
        dto.setStatus(link.getStatus());
        dto.setCreatedAt(link.getCreatedAt());
        dto.setUpdatedAt(link.getUpdatedAt());

        dto.setFullLink(frontendUrl + "/evaluate/" + link.getToken());

        if (link.getEvaluationForm() != null) {
            EvaluationForm form = link.getEvaluationForm();
            dto.setEvaluationFormTitle(form.getTitle());
            if (form.getEvaluationPeriod() != null) {
                dto.setEvaluationPeriodTitle(form.getEvaluationPeriod().getTitle());
            }
        }

        // Count submissions for this link
        try {
            List<EvaluationSubmission> submissions = 
                    submissionRepository.findByEvaluationLinkId(link.getId());
            dto.setSubmissionCount(submissions != null ? submissions.size() : 0);
        } catch (Exception e) {
            dto.setSubmissionCount(0);
        }

        return dto;
    }
}