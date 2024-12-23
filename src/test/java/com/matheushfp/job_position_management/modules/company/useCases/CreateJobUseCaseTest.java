package com.matheushfp.job_position_management.modules.company.useCases;

import com.matheushfp.job_position_management.exceptions.UserNotFoundException;
import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.entities.JobEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
import com.matheushfp.job_position_management.modules.company.repositories.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateJobUseCaseTest {

    @InjectMocks
    private CreateJobUseCase createJobUseCase;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("Should not be able to Create a Job if Company Not Found")
    void shouldNotBeAbleToCreateJobIfCompanyNotFound() {
        JobEntity job = new JobEntity();
        job.setTitle("job_title");
        job.setLevel("job_level");
        job.setBenefits("job_benefits");
        job.setDescription("job_description");

        assertThrows(UserNotFoundException.class, () -> {
            createJobUseCase.execute(job);
        });
    }

    @Test
    @DisplayName("Should be able to Create a new Job")
    void shouldBeAbleToCreateJob() {
        CompanyEntity company = new CompanyEntity();
        company.setUsername("company_username");
        company.setName("company_name");
        company.setEmail("company@mail.com");
        company.setPassword("123456");

        when(companyRepository.save(company)).thenReturn(company);

        companyRepository.save(company);

        when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));

        JobEntity job = new JobEntity();
        job.setTitle("job_title");
        job.setLevel("job_level");
        job.setBenefits("job_benefits");
        job.setDescription("job_description");

        when(jobRepository.save(job)).thenReturn(job);

        var result = createJobUseCase.execute(job);

        assertThat(result).hasFieldOrProperty("id");
    }

}