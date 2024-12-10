package com.matheushfp.job_position_management.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.matheushfp.job_position_management.providers.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(null);
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            DecodedJWT token = jwtProvider.validateToken(authHeader);

            if(token != null) {
                String sub = token.getSubject();
                Instant expiresAt = token.getExpiresAtAsInstant();
                Instant now = Instant.now();

                if (sub.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }

                if (expiresAt.isBefore(now)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }

                request.setAttribute("userId", sub);

                List<String> roles = token.getClaim("roles").asList(String.class);

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map((role) -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(sub, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

            }

        }

        filterChain.doFilter(request, response);
    }
}
