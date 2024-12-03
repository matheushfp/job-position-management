package com.matheushfp.job_position_management.modules.candidate.controllers;

import com.matheushfp.job_position_management.exceptions.CandidateAlreadyExistsException;
import com.matheushfp.job_position_management.exceptions.ErrorMessageDTO;
import com.matheushfp.job_position_management.modules.candidate.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.useCases.CreateCandidateUseCase;
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
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidateEntity) {
        try {
            CandidateEntity candidate = this.createCandidateUseCase.execute(candidateEntity);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(candidate.getId())
                    .toUri();

            return ResponseEntity.created(location).body(candidate);
        } catch(CandidateAlreadyExistsException e) {
            ErrorMessageDTO errorMessage = new ErrorMessageDTO(e.getMessage());

            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }

}
