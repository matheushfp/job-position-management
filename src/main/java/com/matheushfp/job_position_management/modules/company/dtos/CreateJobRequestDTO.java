package com.matheushfp.job_position_management.modules.company.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateJobRequestDTO {

    @NotBlank(message = "[title] should be specified")
    private String title;

    @NotBlank(message = "[level] should be specified")
    private String level;

    private String description;
    private String benefits;
}
