// src/main/java/com/example/evaluationsystem/service/EvaluationLinkService.java
package com.example.evaluationsystem.service;

import com.example.evaluationsystem.dto.EvaluationLinkDTO;
import com.example.evaluationsystem.dto.EvaluationLinkRequestDTO;

import java.util.List;

public interface EvaluationLinkService {
    EvaluationLinkDTO createLink(EvaluationLinkRequestDTO requestDTO);
    EvaluationLinkDTO getLinkById(Long id);
    EvaluationLinkDTO getLinkByToken(String token);
    List<EvaluationLinkDTO> getLinksByFormId(Long formId);  // Changed from getLinksByPeriodId
    List<EvaluationLinkDTO> getAllLinks();
    void deactivateLink(Long id);
    void deleteLink(Long id);
    boolean isValidToken(String token);
}