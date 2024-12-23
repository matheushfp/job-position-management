package com.matheushfp.job_position_management.modules.company.useCases;

import com.matheushfp.job_position_management.dtos.AuthRequestDTO;
import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
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
class AuthCompanyUseCaseTest {

    @InjectMocks
    private AuthCompanyUseCase authCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthRequestDTO authCompany;

    @BeforeEach
    void createAuthRequest() {
        authCompany = new AuthRequestDTO("company", "123456");

        // Injecting secret value
        ReflectionTestUtils.setField(authCompanyUseCase, "secret", "default-secret");
    }

    @Test
    @DisplayName("Should not be able to Auth Company if it Not Exists")
    void shouldNotBeAbleToAuthCompanyIfItNotExists() {
        when(companyRepository.findByUsername(authCompany.username())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> {
            authCompanyUseCase.execute(authCompany);
        });
    }

    @Test
    @DisplayName("Should not be able to Auth Company if password is wrong")
    void shouldNotBeAbleToAuthCompanyIfPasswordIsWrong() {
        when(companyRepository.findByUsername(authCompany.username())).thenReturn(Optional.of(new CompanyEntity()));

        when(passwordEncoder.matches(authCompany.password(), null)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            authCompanyUseCase.execute(authCompany);
        });
    }

    @Test
    @DisplayName("Should be able to Auth a Company")
    void shouldBeAbleToAuthCompany() {
        CompanyEntity company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        company.setUsername(authCompany.username());

        when(companyRepository.findByUsername(authCompany.username())).thenReturn(Optional.of(company));

        when(passwordEncoder.matches(authCompany.password(), null)).thenReturn(true);

        var result = authCompanyUseCase.execute(authCompany);

        assertThat(result).startsWith("eyJ");
    }
}