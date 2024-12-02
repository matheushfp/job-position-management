package com.matheushfp.job_position_management.exceptions;

public class CompanyAlreadyExistsException extends RuntimeException {
    public CompanyAlreadyExistsException() {
        super("Company Already Exists");
    }
}
