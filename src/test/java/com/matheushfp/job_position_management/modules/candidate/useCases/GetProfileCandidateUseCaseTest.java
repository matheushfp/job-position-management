package com.matheushfp.job_position_management.modules.candidate.useCases;

import com.matheushfp.job_position_management.exceptions.UserNotFoundException;
import com.matheushfp.job_position_management.modules.candidate.entities.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.repositories.CandidateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProfileCandidateUseCaseTest {

    @InjectMocks
    private GetProfileCandidateUseCase getProfileCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Test
    @DisplayName("Should not be able to Get Profile if Candidate doesn't exists")
    void shouldNotBeAbleToGetProfileIfCandidateNotExists() {
        UUID candidateId = UUID.randomUUID();

        assertThrows(UserNotFoundException.class, () -> {
            getProfileCandidateUseCase.execute(candidateId);
        });
    }

    @Test
    @DisplayName("Should be able to Get Candidate Profile")
    void shouldBeAbleToGetProfile() {
        UUID candidateId = UUID.randomUUID();

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(new CandidateEntity()));

        var result = getProfileCandidateUseCase.execute(candidateId);

        assertThat(result).hasFieldOrProperty("name");
        assertThat(result).hasFieldOrProperty("username");

    }
}