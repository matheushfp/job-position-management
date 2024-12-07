package com.matheushfp.job_position_management.modules.candidate.controllers;

import com.matheushfp.job_position_management.exceptions.CandidateAlreadyExistsException;
import com.matheushfp.job_position_management.dtos.ErrorMessageDTO;
import com.matheushfp.job_position_management.modules.candidate.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.useCases.CreateCandidateUseCase;
import com.matheushfp.job_position_management.modules.candidate.useCases.GetProfileCandidateUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private GetProfileCandidateUseCase getProfileCandidateUseCase;

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

    @GetMapping("/me")
    public ResponseEntity<Object> getProfile(HttpServletRequest request) {
        var candidateId = request.getAttribute("userId");

        try {
            var candidateProfile = this.getProfileCandidateUseCase.execute(UUID.fromString(candidateId.toString()));

            return ResponseEntity.ok(candidateProfile);
        } catch(RuntimeException e) {
            ErrorMessageDTO errorMessage = new ErrorMessageDTO(e.getMessage());

            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }

    }

}
