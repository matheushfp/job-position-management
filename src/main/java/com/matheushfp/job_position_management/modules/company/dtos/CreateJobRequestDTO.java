package com.matheushfp.job_position_management.modules.company.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateJobRequestDTO {

    @Schema(example = "Junior Back-end Developer (Java)")
    @NotBlank(message = "[title] should be specified")
    private String title;

    @Schema(example = "JUNIOR")
    @NotBlank(message = "[level] should be specified")
    private String level;

    @Schema(example = "Back-end Java Developer with 1-2 years of experience. Knowledge in Spring, Hibernate and APIs are mandatory.")
    private String description;

    @Schema(example = "Medical Coverage, WellHub.")
    private String benefits;
}
