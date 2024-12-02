package com.matheushfp.job_position_management.exceptions;

public class CandidateAlreadyExistsException extends RuntimeException {
    public CandidateAlreadyExistsException() {
        super("Candidate Already Exists");
    }
}
