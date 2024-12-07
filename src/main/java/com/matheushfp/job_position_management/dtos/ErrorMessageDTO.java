package com.matheushfp.job_position_management.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessageDTO {
    private String message;
    private String field;

    public ErrorMessageDTO(String message) {
        this.message = message;
    }
}
