package com.matheushfp.job_position_management.modules.candidate.useCases;

import com.matheushfp.job_position_management.exceptions.JobNotFoundException;
import com.matheushfp.job_position_management.exceptions.UserNotFoundException;
import com.matheushfp.job_position_management.modules.candidate.entities.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.entities.JobApplicationEntity;
import com.matheushfp.job_position_management.modules.candidate.repositories.CandidateRepository;
import com.matheushfp.job_position_management.modules.candidate.repositories.JobApplicationRepository;
import com.matheushfp.job_position_management.modules.company.entities.JobEntity;
import com.matheushfp.job_position_management.modules.company.repositories.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplyForJobUseCaseTest {

    @InjectMocks
    private ApplyForJobUseCase applyForJobUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Test
    @DisplayName("Should not be able to apply for Job if Candidate doesn't exists")
    void shouldNotBeAbleToApplyForJobIfCandidateNotExists() {
        assertThrows(UserNotFoundException.class, () -> {
            applyForJobUseCase.execute(null, null);
        });
    }

    @Test
    @DisplayName("Should not be able to apply for Job if it doesn't exists")
    void shouldNotBeAbleToApplyForJobIfJobNotExists() {
        UUID candidateId = UUID.randomUUID();

        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(candidateId);

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

        assertThrows(JobNotFoundException.class, () -> {
            applyForJobUseCase.execute(candidateId, null);
        });
    }

    @Test
    @DisplayName("Should be able to apply for Job")
    void shouldBeAbleToApplyForJob() {
        UUID candidateId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();

        var jobApplication = new JobApplicationEntity(candidateId, jobId);

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(new CandidateEntity()));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(new JobEntity()));
        when(jobApplicationRepository.save(jobApplication)).thenReturn(jobApplication);

        var result = applyForJobUseCase.execute(candidateId, jobId);

        assertThat(result).hasFieldOrProperty("id");
    }
}
