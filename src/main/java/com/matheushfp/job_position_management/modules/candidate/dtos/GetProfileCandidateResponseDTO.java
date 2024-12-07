package com.matheushfp.job_position_management.modules.candidate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class GetProfileCandidateResponseDTO {
    private UUID id;
    private String username;
    private String name;
    private String email;
    private String description;
}
