package com.matheushfp.job_position_management.modules.candidate.controllers;

import com.matheushfp.job_position_management.exceptions.CandidateAlreadyExistsException;
import com.matheushfp.job_position_management.dtos.ErrorMessageDTO;
import com.matheushfp.job_position_management.modules.candidate.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.dtos.GetProfileCandidateResponseDTO;
import com.matheushfp.job_position_management.modules.candidate.useCases.CreateCandidateUseCase;
import com.matheushfp.job_position_management.modules.candidate.useCases.GetProfileCandidateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/candidates")
@Tag(name = "Candidates")
public class CandidateController {

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private GetProfileCandidateUseCase getProfileCandidateUseCase;

    @PostMapping
    @Operation(summary = "Create a Candidate", description = "Create a Candidate")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = @Content(schema = @Schema(implementation = CandidateEntity.class))),
            @ApiResponse(responseCode = "409",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Candidate Already Exists\" }")))
    })
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
    @PreAuthorize("hasRole('CANDIDATE')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get Candidate Profile", description = "Get Candidate Profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(schema = @Schema(implementation = GetProfileCandidateResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(example = "{ message: \"Candidate Not Found\"}"))),
            @ApiResponse(responseCode = "403")
    })
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
