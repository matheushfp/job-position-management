package com.matheushfp.job_position_management.modules.company.controllers;

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

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Create a Job", description = "Create a job position related to a company")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<JobEntity> create(@Valid @RequestBody CreateJobRequestDTO body, HttpServletRequest request) {
        var companyId = request.getAttribute("userId");

        JobEntity jobEntity = JobEntity.builder()
                .title(body.getTitle())
                .level(body.getLevel())
                .description(body.getDescription())
                .benefits(body.getBenefits())
                .companyId(UUID.fromString(companyId.toString()))
                .build();

        var job = this.createJobUseCase.execute(jobEntity);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(job.getId())
                .toUri();

        return ResponseEntity.created(location).body(job);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Search Jobs by query", description = "Candidates can search Jobs by query")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Object.class)))
    })
    public List<JobEntity> search(@RequestParam String query) {
        return this.searchJobsByQueryUseCase.execute(query);
    }
}
