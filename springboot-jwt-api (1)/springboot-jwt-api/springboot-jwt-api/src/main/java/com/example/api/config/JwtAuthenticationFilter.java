package com.example.api.config;

import com.example.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                userRepo.findByUsername(username).ifPresent(user -> {
                    // VALIDACIÓN AGREGADA
                    if (jwtService.isTokenValid(token, user.getUsername())) {
                        var authorities = user.getRoles().stream()
                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                                .collect(Collectors.toList());

                        var authToken = new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                authorities
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                });
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        chain.doFilter(request, response);
    }
}