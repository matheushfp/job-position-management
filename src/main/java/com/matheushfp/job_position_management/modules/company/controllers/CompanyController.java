package com.matheushfp.job_position_management.modules.company.controllers;

import com.matheushfp.job_position_management.exceptions.CompanyAlreadyExistsException;
import com.matheushfp.job_position_management.dtos.ErrorMessageDTO;
import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.useCases.CreateCompanyUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("companies")
public class CompanyController {

    @Autowired
    private CreateCompanyUseCase createCompanyUseCase;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CompanyEntity companyEntity) {
        try {
            CompanyEntity company = this.createCompanyUseCase.execute(companyEntity);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(company.getId())
                    .toUri();

            return ResponseEntity.created(location).body(company);
        } catch (CompanyAlreadyExistsException e) {
            ErrorMessageDTO errorMessage = new ErrorMessageDTO(e.getMessage());

            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }
}
