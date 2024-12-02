package com.matheushfp.job_position_management.modules.company.useCases;

import com.matheushfp.job_position_management.exceptions.CompanyAlreadyExistsException;
import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCompanyUseCase {

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyEntity execute(CompanyEntity companyEntity) {
        this.companyRepository
                .findByUsernameOrEmail(companyEntity.getUsername(), companyEntity.getEmail())
                .ifPresent((company) -> {
                    throw new CompanyAlreadyExistsException();
                });

        return this.companyRepository.save(companyEntity);
    }
}
