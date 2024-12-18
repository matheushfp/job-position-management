package com.matheushfp.job_position_management.modules.company.useCases;

import com.matheushfp.job_position_management.exceptions.UserNotFoundException;
import com.matheushfp.job_position_management.modules.company.entities.JobEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
import com.matheushfp.job_position_management.modules.company.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateJobUseCase {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public JobEntity execute(JobEntity jobEntity) {
        this.companyRepository.findById(jobEntity.getCompanyId())
                .orElseThrow(() -> new UserNotFoundException("Company Not Found"));

        return this.jobRepository.save(jobEntity);
    }
}
