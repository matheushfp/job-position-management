package com.matheushfp.job_position_management.security;

import com.matheushfp.job_position_management.providers.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(null);
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            String sub = jwtProvider.validateToken(authHeader);

            if (sub.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

            request.setAttribute("userId", sub);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(sub, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        filterChain.doFilter(request, response);
    }
}
