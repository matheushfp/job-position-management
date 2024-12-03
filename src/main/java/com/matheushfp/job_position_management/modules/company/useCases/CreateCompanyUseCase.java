package com.matheushfp.job_position_management.modules.company.useCases;

import com.matheushfp.job_position_management.exceptions.CompanyAlreadyExistsException;
import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateCompanyUseCase {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CompanyEntity execute(CompanyEntity companyEntity) {
        this.companyRepository
                .findByUsernameOrEmail(companyEntity.getUsername(), companyEntity.getEmail())
                .ifPresent((company) -> {
                    throw new CompanyAlreadyExistsException();
                });

        String passwordHash = passwordEncoder.encode(companyEntity.getPassword());
        companyEntity.setPassword(passwordHash);

        return this.companyRepository.save(companyEntity);
    }
}
