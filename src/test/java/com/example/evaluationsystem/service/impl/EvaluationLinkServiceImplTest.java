package com.example.evaluationsystem.service.impl;

import com.example.evaluationsystem.model.EvaluationForm;
import com.example.evaluationsystem.model.EvaluationLink;
import com.example.evaluationsystem.model.EvaluationPeriod;
import com.example.evaluationsystem.repository.EvaluationFormRepository;
import com.example.evaluationsystem.repository.EvaluationLinkRepository;
import com.example.evaluationsystem.repository.EvaluationPeriodRepository;
import com.example.evaluationsystem.repository.EvaluationSubmissionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class EvaluationLinkServiceImplTest {

    @Test
    void isValidToken_shouldReturnFalseWhenEvaluationPeriodHasExpired() {
        EvaluationLinkRepository linkRepository = Mockito.mock(EvaluationLinkRepository.class);
        EvaluationFormRepository formRepository = Mockito.mock(EvaluationFormRepository.class);
        EvaluationPeriodRepository periodRepository = Mockito.mock(EvaluationPeriodRepository.class);
        EvaluationSubmissionRepository submissionRepository = Mockito.mock(EvaluationSubmissionRepository.class);
        EvaluationLinkServiceImpl service = new EvaluationLinkServiceImpl(linkRepository, formRepository, periodRepository, submissionRepository);

        EvaluationLink link = new EvaluationLink();
        link.setId(1L);
        link.setToken("abc123");
        link.setStatus("active");
        link.setEvaluationFormId(10L);

        EvaluationForm form = new EvaluationForm();
        form.setId(10L);
        form.setEvaluationPeriodId(20L);

        EvaluationPeriod period = new EvaluationPeriod();
        period.setId(20L);
        period.setStatus("active");
        period.setStartDate(LocalDate.now().minusDays(10));
        period.setEndDate(LocalDate.now().minusDays(1));

        form.setEvaluationPeriod(period);
        link.setEvaluationForm(form);

        when(linkRepository.findByToken("abc123")).thenReturn(Optional.of(link));
        when(linkRepository.findByTokenWithDetails("abc123")).thenReturn(Optional.of(link));

        boolean valid = service.isValidToken("abc123");

        assertThat(valid).isFalse();
    }
}
