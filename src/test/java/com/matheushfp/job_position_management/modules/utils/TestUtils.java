package com.matheushfp.job_position_management.modules.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TestUtils {
    public static String objectToJson(Object object) {
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateToken(UUID companyId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("default-secret");
            String token = JWT.create()
                    .withIssuer("job_position_management_spring")
                    .withSubject(companyId.toString())
                    .withExpiresAt(Instant.now().plus(Duration.ofHours(1)))
                    .withClaim("roles", List.of("COMPANY"))
                    .sign(algorithm);

            return token;
        } catch(JWTCreationException e) {
            throw new JWTCreationException("Error generating token", e);
        }
    }
}
