package com.matheushfp.job_position_management.modules.candidate.controllers;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.matheushfp.job_position_management.dtos.ErrorMessageDTO;
import com.matheushfp.job_position_management.dtos.AuthRequestDTO;
import com.matheushfp.job_position_management.modules.candidate.useCases.AuthCandidateUseCase;
import com.matheushfp.job_position_management.dtos.AuthResponseDTO;
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
public class AuthCandidateController {

    @Autowired
    private AuthCandidateUseCase authCandidateUseCase;

    @PostMapping("/candidate")
    public ResponseEntity<Object> authenticate(@RequestBody AuthRequestDTO authCandidate) {

        try{
            String token = this.authCandidateUseCase.execute(authCandidate);

            AuthResponseDTO authResponse = new AuthResponseDTO(token);

            return ResponseEntity.ok(authResponse);
        } catch(BadCredentialsException | JWTCreationException e) {
            ErrorMessageDTO errorMessage = new ErrorMessageDTO(e.getMessage());

            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }

    }

}
