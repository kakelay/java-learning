package com.in28minutes.springboot.myfirstwebapp.integration;

import com.in28minutes.springboot.myfirstwebapp.common.RequestReference;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InboundBearerTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(InboundBearerTokenFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthService authService;
    private final TokenManager tokenManager;

    public InboundBearerTokenFilter(AuthService authService, TokenManager tokenManager) {
        this.authService = authService;
        this.tokenManager = tokenManager;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // boolean isAccountLookup = uri.startsWith("/api/accounts");
        // boolean isUserApi = uri.startsWith("/api/user") ||
        // uri.startsWith("/api/user/");
        return HttpMethod.OPTIONS.matches(request.getMethod())
                || !uri.startsWith("/api/");
        // || isAccountLookup
        // || isUserApi;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            reject(response, "Missing bearer token");
            return;
        }

        try {
            authService.getValidToken();
        } catch (AuthenticationException ex) {
            logger.warn("Authentication service unavailable for {} {}: {}",
                    request.getMethod(), request.getRequestURI(), ex.getMessage());
            reject(response, "Authentication service unavailable");
            return;
        }

        String token = authorization.substring(BEARER_PREFIX.length());
        if (!tokenManager.matchesValidToken(token)) {
            logger.warn("Rejected unauthorized API request: {} {}", request.getMethod(), request.getRequestURI());
            reject(response, "Invalid or expired bearer token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void reject(HttpServletResponse response, String message) throws IOException {
        String referenceNumber = RequestReference.getOrCreate();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"referenceNumber\":\"" + referenceNumber
                + "\",\"responseCode\":\"AUTH001\",\"status\":\"Fail\",\"message\":\"" + message + "\"}");
    }
}
