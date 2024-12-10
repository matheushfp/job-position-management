package com.matheushfp.job_position_management.modules.company.useCases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.matheushfp.job_position_management.dtos.AuthRequestDTO;
import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class AuthCompanyUseCase {

    @Value("${security.token.secret}")
    private String secret;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String execute(AuthRequestDTO authCompany) {
        CompanyEntity company = this.companyRepository.findByUsername(authCompany.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid Credentials"));

        boolean passwordMatches = this.passwordEncoder.matches(authCompany.password(), company.getPassword());

        if(!passwordMatches) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("job_position_management_spring")
                    .withSubject(company.getId().toString())
                    .withExpiresAt(Instant.now().plus(Duration.ofHours(1)))
                    .withClaim("roles", List.of("COMPANY"))
                    .sign(algorithm);

            return token;
        } catch(JWTCreationException e) {
            throw new JWTCreationException("Error generating token", e);
        }
    }
}
