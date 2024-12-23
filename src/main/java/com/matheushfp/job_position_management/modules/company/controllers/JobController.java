package com.matheushfp.job_position_management.modules.company.controllers;

import com.matheushfp.job_position_management.dtos.ErrorMessageDTO;
import com.matheushfp.job_position_management.exceptions.JobNotFoundException;
import com.matheushfp.job_position_management.exceptions.UserNotFoundException;
import com.matheushfp.job_position_management.modules.candidate.entities.JobApplicationEntity;
import com.matheushfp.job_position_management.modules.candidate.useCases.ApplyForJobUseCase;
import com.matheushfp.job_position_management.modules.company.dtos.CreateJobRequestDTO;
import com.matheushfp.job_position_management.modules.company.entities.JobEntity;
import com.matheushfp.job_position_management.modules.company.useCases.CreateJobUseCase;
import com.matheushfp.job_position_management.modules.candidate.useCases.SearchJobsByQueryUseCase;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jobs")
@Tag(name = "Jobs")
@SecurityRequirement(name = "bearerAuth")
public class JobController {

    @Autowired
    CreateJobUseCase createJobUseCase;

    @Autowired
    SearchJobsByQueryUseCase searchJobsByQueryUseCase;

    @Autowired
    ApplyForJobUseCase applyForJobUseCase;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Create a Job", description = "Create a job position related to a company")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateJobRequestDTO body, HttpServletRequest request) {
        var companyId = request.getAttribute("userId");

        JobEntity jobEntity = JobEntity.builder()
                .title(body.getTitle())
                .level(body.getLevel())
                .description(body.getDescription())
                .benefits(body.getBenefits())
                .companyId(UUID.fromString(companyId.toString()))
                .build();

        try {
            var job = this.createJobUseCase.execute(jobEntity);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(job.getId())
                    .toUri();

            return ResponseEntity.created(location).body(job);
        } catch (UserNotFoundException e) {
            ErrorMessageDTO errorMessage = new ErrorMessageDTO(e.getMessage());

            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Search Jobs by query", description = "Candidates can search Jobs by query")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Object.class)))
    })
    public List<JobEntity> search(@RequestParam String query) {
        return this.searchJobsByQueryUseCase.execute(query);
    }

    @PostMapping("/{jobId}/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Apply for a Job", description = "Candidates can apply for a Job")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = @Content(schema = @Schema(implementation = JobApplicationEntity.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = ErrorMessageDTO.class))),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErrorMessageDTO.class)))
    })
    public ResponseEntity<Object> apply(@PathVariable UUID jobId, HttpServletRequest request) {
        var candidateId = request.getAttribute("userId");

        try {
            var jobApplication = this.applyForJobUseCase.execute(UUID.fromString(candidateId.toString()), jobId);

            return ResponseEntity.status(HttpStatus.CREATED).body(jobApplication);

        } catch (UserNotFoundException | JobNotFoundException e) {
            ErrorMessageDTO errorMessage = new ErrorMessageDTO(e.getMessage());

            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }

    }
}
