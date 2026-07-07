// src/main/java/com/example/evaluationsystem/config/JwtAuthenticationFilter.java
package com.example.evaluationsystem.config;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") || path.startsWith("/auth/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("JWT filter: no Bearer header on {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        // Skip processing if token is empty or too short to be a valid JWT
        if (jwt.isEmpty() || jwt.length() < 10) {
            logger.warn("JWT filter: token empty/too short on {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (MalformedJwtException e) {
            logger.warn("JWT filter: malformed token on {} {} - {}", request.getMethod(), request.getRequestURI(), e.getMessage());
            filterChain.doFilter(request, response);
            return;
        } catch (Exception e) {
            logger.warn("JWT filter: {} on {} {} - {}", e.getClass().getSimpleName(), request.getMethod(), request.getRequestURI(), e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (userEmail == null) {
            logger.warn("JWT filter: extracted userEmail is null on {} {}", request.getMethod(), request.getRequestURI());
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            boolean valid = jwtService.isTokenValid(jwt, userDetails);
            logger.info("JWT filter: userEmail={} tokenValid={} on {} {}", userEmail, valid, request.getMethod(), request.getRequestURI());

            if (valid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("JWT filter: SecurityContext set for {}", userEmail);
            }
        }
        filterChain.doFilter(request, response);
    }
}