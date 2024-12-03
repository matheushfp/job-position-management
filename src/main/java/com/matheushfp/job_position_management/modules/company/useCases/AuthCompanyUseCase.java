package com.matheushfp.job_position_management.modules.company.useCases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.matheushfp.job_position_management.modules.company.dtos.AuthCompanyDTO;
import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthCompanyUseCase {

    @Value("${security.token.secret}")
    private String secret;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String execute(AuthCompanyDTO authCompanyDTO) {
        CompanyEntity company = this.companyRepository.findByUsername(authCompanyDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Company Not Found"));

        boolean passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        if(!passwordMatches) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("job_position_management_spring")
                    .withSubject(company.getId().toString())
                    .sign(algorithm);

            return token;
        } catch(JWTCreationException e) {
            throw new JWTCreationException("Error generating token", e);
        }
    }
}
