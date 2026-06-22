package com.in28minutes.springboot.myfirstwebapp.filter;

import com.in28minutes.springboot.myfirstwebapp.common.RequestReference;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final int MAX_BODY_LENGTH = 4_000;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !(path.startsWith("/api/") || path.equals("/auth/sign-in"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String referenceNumber = RequestReference.bind(request);
        MDC.put(RequestReference.MDC_KEY, referenceNumber);

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        long startedAt = System.currentTimeMillis();

        logger.info("HTTP request started | referenceNumber={} | method={} | path={} | query={} | clientIp={} | headers={}",
                referenceNumber, request.getMethod(), request.getRequestURI(), valueOrDash(request.getQueryString()),
                clientIp(request), headers(request));

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long durationMs = System.currentTimeMillis() - startedAt;
            logger.info("HTTP request body | referenceNumber={} | body={}", referenceNumber,
                    body(requestWrapper.getContentAsByteArray()));
            logger.info("HTTP response completed | referenceNumber={} | status={} | durationMs={} | body={}",
                    referenceNumber, responseWrapper.getStatus(), durationMs,
                    body(responseWrapper.getContentAsByteArray()));
            responseWrapper.copyBodyToResponse();
            MDC.remove(RequestReference.MDC_KEY);
        }
    }

    private String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String headers(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder("{");
        List<String> headerNames = Collections.list(request.getHeaderNames());
        for (int i = 0; i < headerNames.size(); i++) {
            String name = headerNames.get(i);
            builder.append(name).append("=");
            if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
                builder.append("***");
            } else {
                builder.append(request.getHeader(name));
            }
            if (i < headerNames.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    private String body(byte[] content) {
        if (content == null || content.length == 0) {
            return "-";
        }
        String body = new String(content, StandardCharsets.UTF_8);
        body = maskSensitiveFields(body);
        if (body.length() > MAX_BODY_LENGTH) {
            return body.substring(0, MAX_BODY_LENGTH) + "...(truncated)";
        }
        return body;
    }

    private String maskSensitiveFields(String value) {
        return value
                .replaceAll("(?i)(\"password\"\\s*:\\s*\")[^\"]*(\")", "$1***$2")
                .replaceAll("(?i)(\"accessToken\"\\s*:\\s*\")[^\"]*(\")", "$1***$2");
    }

    private String valueOrDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
