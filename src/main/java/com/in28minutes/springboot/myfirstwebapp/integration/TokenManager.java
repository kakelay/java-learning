package com.in28minutes.springboot.myfirstwebapp.integration;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class TokenManager {

    private final AtomicReference<TokenState> tokenState = new AtomicReference<>(TokenState.empty());

    public String getToken() {
        return tokenState.get().token();
    }

    public boolean hasValidToken() {
        TokenState current = tokenState.get();
        return current.token() != null
                && !current.token().isBlank()
                && (current.expiresAt() == null || Instant.now().isBefore(current.expiresAt()));
    }

    public boolean matchesValidToken(String token) {
        if (!hasValidToken() || token == null || token.isBlank()) {
            return false;
        }
        byte[] expected = tokenState.get().token().getBytes(StandardCharsets.UTF_8);
        byte[] actual = token.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(expected, actual);
    }

    public void storeToken(String token, Instant expiresAt) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Access token cannot be empty");
        }
        tokenState.set(new TokenState(token, expiresAt));
    }

    public void clear() {
        tokenState.set(TokenState.empty());
    }

    private record TokenState(String token, Instant expiresAt) {
        private static TokenState empty() {
            return new TokenState(null, null);
        }
    }
}
