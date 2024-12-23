package com.matheushfp.job_position_management.modules.candidate.useCases;

import com.matheushfp.job_position_management.dtos.AuthRequestDTO;
import com.matheushfp.job_position_management.modules.candidate.entities.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.repositories.CandidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthCandidateUseCaseTest {

    @InjectMocks
    private AuthCandidateUseCase authCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthRequestDTO authCandidate;

    @BeforeEach
    void createAuthRequest() {
        authCandidate = new AuthRequestDTO("johndoe", "123456");

        // Injecting secret value
        ReflectionTestUtils.setField(authCandidateUseCase, "secret", "default-secret");
    }

    @Test
    @DisplayName("Should not be able to Auth a Candidate if it doesn't exist")
    void shouldNotBeAbleToAuthCandidateIfItNotExists() {
        when(candidateRepository.findByUsername(authCandidate.username())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> {
            authCandidateUseCase.execute(authCandidate);
        });
    }

    @Test
    @DisplayName("Should not be able to Auth a Candidate if password is wrong")
    void shouldNotBeAbleToAuthCandidateWithWrongPassword() {
        when(candidateRepository.findByUsername(authCandidate.username()))
                .thenReturn(Optional.of(new CandidateEntity()));

        when(passwordEncoder.matches(authCandidate.password(), null))
                .thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            authCandidateUseCase.execute(authCandidate);
        });
    }

    @Test
    @DisplayName("Should be able to Auth a Candidate")
    void shouldBeAbleToAuthCandidate() {
        CandidateEntity candidate = new CandidateEntity();
        candidate.setId(UUID.randomUUID());
        candidate.setUsername(authCandidate.username());

        when(candidateRepository.findByUsername(authCandidate.username()))
                .thenReturn(Optional.of(candidate));

        when(passwordEncoder.matches(authCandidate.password(), null))
                .thenReturn(true);

        var result = authCandidateUseCase.execute(authCandidate);

        assertThat(result).startsWith("eyJ");
    }

}