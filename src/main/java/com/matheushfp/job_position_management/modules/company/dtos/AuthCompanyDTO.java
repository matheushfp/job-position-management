package com.matheushfp.job_position_management.modules.company.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCompanyDTO {
    private String username;
    private String password;
}
