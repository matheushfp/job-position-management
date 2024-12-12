package com.matheushfp.job_position_management.modules.company.controllers;

import com.matheushfp.job_position_management.modules.company.dtos.CreateJobRequestDTO;
import com.matheushfp.job_position_management.modules.company.entities.JobEntity;
import com.matheushfp.job_position_management.modules.company.useCases.CreateJobUseCase;
import com.matheushfp.job_position_management.modules.company.useCases.SearchJobsByQueryUseCase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public JobEntity create(@Valid @RequestBody CreateJobRequestDTO body, HttpServletRequest request) {
        var companyId = request.getAttribute("userId");

        JobEntity jobEntity = JobEntity.builder()
                .title(body.getTitle())
                .level(body.getLevel())
                .description(body.getDescription())
                .benefits(body.getBenefits())
                .companyId(UUID.fromString(companyId.toString()))
                .build();

        return this.createJobUseCase.execute(jobEntity);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('CANDIDATE')")
    public List<JobEntity> search(@RequestParam String query) {
        return this.searchJobsByQueryUseCase.execute(query);
    }
}
