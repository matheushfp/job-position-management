package com.matheushfp.job_position_management.exceptions;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException() {
        super("Job Not Found");
    }
}
