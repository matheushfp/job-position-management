package com.matheushfp.job_position_management.modules.candidate.useCases;

import com.matheushfp.job_position_management.exceptions.CandidateAlreadyExistsException;
import com.matheushfp.job_position_management.modules.candidate.entities.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.repositories.CandidateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCandidateUseCaseTest {

    @InjectMocks
    private CreateCandidateUseCase createCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should not be able to Create a Candidate if it already exists")
    void shouldNotBeAbleToCreateCandidateIfAlreadyExists() {
        var candidate = new CandidateEntity();
        candidate.setUsername("johndoe");
        candidate.setEmail("johdoe@mail.com");
        candidate.setName("John Doe");
        candidate.setPassword("12345678");

        when(candidateRepository.findByUsernameOrEmail(candidate.getUsername(), candidate.getEmail()))
                .thenReturn(Optional.of(candidate));

        assertThrows(CandidateAlreadyExistsException.class, () -> {
            createCandidateUseCase.execute(candidate);
        });

    }

    @Test
    @DisplayName("Should be able to Create a Candidate")
    void shouldBeAbleToCreateCandidate() {
        var candidate = new CandidateEntity();
        candidate.setUsername("johndoe");
        candidate.setEmail("johdoe@mail.com");
        candidate.setName("John Doe");
        candidate.setPassword("12345678");

        when(candidateRepository.findByUsernameOrEmail(candidate.getUsername(), candidate.getEmail()))
                .thenReturn(Optional.empty());
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        var result = createCandidateUseCase.execute(candidate);

        assertThat(result).hasFieldOrProperty("id");
    }
}