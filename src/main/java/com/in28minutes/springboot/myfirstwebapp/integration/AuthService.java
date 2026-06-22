package com.in28minutes.springboot.myfirstwebapp.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final Duration DEFAULT_TOKEN_TTL = Duration.ofDays(1);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RestTemplate loginRestTemplate;
    private final ExternalApiProperties properties;
    private final TokenManager tokenManager;

    public AuthService(RestTemplate loginRestTemplate,
                       ExternalApiProperties properties,
                       TokenManager tokenManager) {
        this.loginRestTemplate = loginRestTemplate;
        this.properties = properties;
        this.tokenManager = tokenManager;
    }

    public String getValidToken() {
        if (tokenManager.hasValidToken()) {
            return tokenManager.getToken();
        }
        return authenticate();
    }

    public synchronized String issueLocalToken(String apiKey, String partnerId, String username, String password) {
        validateCredentials(apiKey, partnerId, username, password);

        byte[] tokenBytes = new byte[48];
        SECURE_RANDOM.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
        tokenManager.storeToken(token, Instant.now().plus(DEFAULT_TOKEN_TTL));

        logger.info("Issued local API bearer token valid for 1 day");
        return token;
    }

    public synchronized String authenticate() {
        if (tokenManager.hasValidToken()) {
            return tokenManager.getToken();
        }
        return authenticate(
                properties.getApiKey(),
                properties.getPartnerId(),
                properties.getUsername(),
                properties.getPassword()
        );
    }

    public synchronized String authenticate(String apiKey, String partnerId, String username, String password) {
        tokenManager.clear();

        logger.info("Authenticating with external API");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apiKey", valueOrDefault(apiKey, properties.getApiKey()));
        headers.set("partnerId", valueOrDefault(partnerId, properties.getPartnerId()));

        Map<String, String> body = Map.of(
                "username", valueOrDefault(username, properties.getUsername()),
                "password", valueOrDefault(password, properties.getPassword())
        );

        try {
            ResponseEntity<JsonNode> response = loginRestTemplate.postForEntity(
                    properties.loginUrl(),
                    new HttpEntity<>(body, headers),
                    JsonNode.class
            );

            JsonNode responseBody = response.getBody();
            String accessToken = extractToken(responseBody);
            Instant expiresAt = extractExpiry(responseBody);
            tokenManager.storeToken(accessToken, expiresAt);

            logger.info("External API authentication succeeded");
            return accessToken;
        } catch (RestClientException exception) {
            tokenManager.clear();
            logger.error("External API authentication failed: {}", exception.getMessage());
            throw new AuthenticationException("Unable to authenticate with external API", exception);
        }
    }

    public synchronized String reAuthenticate() {
        logger.info("Refreshing external API token");
        tokenManager.clear();
        return authenticate();
    }

    private String extractToken(JsonNode body) {
        String token = findText(body, "accessToken", "access_token", "token", "jwt");
        if (token == null || token.isBlank()) {
            throw new AuthenticationException("Login response did not contain an access token");
        }
        return token;
    }

    private Instant extractExpiry(JsonNode body) {
        Long expiresIn = findLong(body, "expiresIn", "expires_in");
        if (expiresIn == null || expiresIn <= 0) {
            return Instant.now().plus(DEFAULT_TOKEN_TTL);
        }
        return Instant.now().plusSeconds(Math.max(0, expiresIn - 30));
    }

    private String findText(JsonNode node, String... fieldNames) {
        if (node == null) {
            return null;
        }
        for (String fieldName : fieldNames) {
            JsonNode match = node.findValue(fieldName);
            if (match != null && match.isTextual()) {
                return match.asText();
            }
        }
        return null;
    }

    private Long findLong(JsonNode node, String... fieldNames) {
        if (node == null) {
            return null;
        }
        for (String fieldName : fieldNames) {
            JsonNode match = node.findValue(fieldName);
            if (match != null && match.canConvertToLong()) {
                return match.asLong();
            }
        }
        return null;
    }

    private String valueOrDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private void validateCredentials(String apiKey, String partnerId, String username, String password) {
        if (!properties.getApiKey().equals(valueOrDefault(apiKey, ""))
                || !properties.getPartnerId().equals(valueOrDefault(partnerId, ""))
                || !properties.getUsername().equals(valueOrDefault(username, ""))
                || !properties.getPassword().equals(valueOrDefault(password, ""))) {
            throw new AuthenticationException("Invalid apiKey, partnerId, username, or password");
        }
    }
}
