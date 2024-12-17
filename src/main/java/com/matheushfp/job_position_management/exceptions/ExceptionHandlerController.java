package com.matheushfp.job_position_management.exceptions;

import com.matheushfp.job_position_management.dtos.ErrorMessageDTO;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class ExceptionHandlerController {

    private final MessageSource messageSource;

    public ExceptionHandlerController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorMessageDTO>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorMessageDTO> errors = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(err -> {
            String message = messageSource.getMessage(err, LocaleContextHolder.getLocale());

            ErrorMessageDTO error = new ErrorMessageDTO(message, err.getField());
            errors.add(error);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Void> handleHttpMessageNotReadableException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Void> handleAuthorizationDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String field = e.getName();
        String requiredType = Optional.ofNullable(e.getRequiredType())
                .map(Class::getSimpleName)
                .orElse("parameter");

        String message = "[" + field + "] should be a valid " + requiredType;

        ErrorMessageDTO error = new ErrorMessageDTO(message, field);

        return ResponseEntity.badRequest().body(error);
    }
}
