package com.gl.patientservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("X-User-ID");
        String role =  request.getHeader("X-User-Role");

        System.out.println("User ID: " + userId);
        System.out.println("User Role: " + role);

        if (userId == null || role == null || userId.isBlank()) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Missing or invalid user identity headers"
            );
            return;
        }

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_"+role));
        var authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }
}
