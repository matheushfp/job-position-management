package com.matheushfp.job_position_management.modules.company.useCases;

import com.matheushfp.job_position_management.exceptions.CompanyAlreadyExistsException;
import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
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
class CreateCompanyUseCaseTest {

    @InjectMocks
    private CreateCompanyUseCase createCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should not be able to Create a Company if it already exists")
    void shouldNotBeAbleToCreateCompanyIfAlreadyExists() {
        CompanyEntity company = new CompanyEntity();
        company.setUsername("company_username");
        company.setName("company_name");
        company.setEmail("company@mail.com");
        company.setPassword("123456");

        when(companyRepository.findByUsernameOrEmail(company.getUsername(), company.getEmail()))
                .thenReturn(Optional.of(company));

        assertThrows(CompanyAlreadyExistsException.class, () -> {
            createCompanyUseCase.execute(company);
        });
    }

    @Test
    @DisplayName("Should be able to Create a Company")
    void shouldBeAbleToCreateCompany() {
        CompanyEntity company = new CompanyEntity();
        company.setUsername("company_username");
        company.setName("company_name");
        company.setEmail("company@mail.com");
        company.setPassword("123456");

        when(companyRepository.findByUsernameOrEmail(company.getUsername(), company.getEmail()))
                .thenReturn(Optional.empty());
        when(companyRepository.save(company)).thenReturn(company);

        var result = createCompanyUseCase.execute(company);

        assertThat(result).hasFieldOrProperty("id");
    }
}