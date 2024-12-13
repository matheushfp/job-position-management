package com.matheushfp.job_position_management.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;

    @Schema(example = "3600")
    private long expiresIn;
}
