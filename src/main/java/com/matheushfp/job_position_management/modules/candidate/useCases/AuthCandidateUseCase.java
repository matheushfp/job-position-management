package com.matheushfp.job_position_management.modules.candidate.useCases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.matheushfp.job_position_management.modules.candidate.entities.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.repositories.CandidateRepository;
import com.matheushfp.job_position_management.dtos.AuthRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class AuthCandidateUseCase {

    @Value("${security.token.secret}")
    private String secret;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String execute(AuthRequestDTO authCandidate) {
        CandidateEntity candidate = this.candidateRepository.findByUsername(authCandidate.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid Credentials"));

        boolean passwordMatches = this.passwordEncoder.matches(authCandidate.password(), candidate.getPassword());

        if(!passwordMatches) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("job_position_management_spring")
                    .withSubject(candidate.getId().toString())
                    .withExpiresAt(Instant.now().plus(Duration.ofHours(1)))
                    .withClaim("roles", List.of("CANDIDATE"))
                    .sign(algorithm);

            return token;
        } catch(JWTCreationException e) {
            throw new JWTCreationException("Error generating token", e);
        }
    }

}
