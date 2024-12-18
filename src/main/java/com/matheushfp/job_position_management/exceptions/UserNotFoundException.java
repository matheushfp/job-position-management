package com.matheushfp.job_position_management.exceptions;

public class UserNotFoundException extends RuntimeException  {
    public UserNotFoundException() {
        super("User Not Found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
