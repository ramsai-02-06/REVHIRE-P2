package com.revhire.revhirep2.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/")) {

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");

            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", 403);
            body.put("error", "Forbidden");
            body.put("message", "You do not have permission to access this resource");
            body.put("path", requestURI);

            objectMapper.writeValue(response.getOutputStream(), body);
        } else {
            response.sendRedirect("/access-denied");
        }
    }
}