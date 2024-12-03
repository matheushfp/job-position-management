package com.matheushfp.job_position_management.modules.company.controllers;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.matheushfp.job_position_management.exceptions.ErrorMessageDTO;
import com.matheushfp.job_position_management.modules.company.dtos.AuthCompanyDTO;
import com.matheushfp.job_position_management.modules.company.dtos.AuthResponseDTO;
import com.matheushfp.job_position_management.modules.company.useCases.AuthCompanyUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthCompanyController {

    @Autowired
    private AuthCompanyUseCase authCompanyUseCase;

    @PostMapping("/company")
    public ResponseEntity<Object> authenticate(@RequestBody AuthCompanyDTO authCompanyDTO) {

        try {
            String token = this.authCompanyUseCase.execute(authCompanyDTO);

            AuthResponseDTO authResponse = new AuthResponseDTO(token);

            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException | JWTCreationException e) {
            ErrorMessageDTO errorMessage = new ErrorMessageDTO(e.getMessage());

            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }
    }
}
