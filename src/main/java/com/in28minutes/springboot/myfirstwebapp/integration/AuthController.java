package com.in28minutes.springboot.myfirstwebapp.integration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> signIn(
            @RequestHeader(value = "apiKey", required = false) String apiKey,
            @RequestHeader(value = "partnerId", required = false) String partnerId,
            @RequestHeader(value = "partnerid", required = false) String partnerIdLowercase,
            @RequestBody(required = false) SignInRequest request) {
        String token = authService.issueLocalToken(
                apiKey,
                partnerId != null ? partnerId : partnerIdLowercase,
                request != null ? request.username() : null,
                request != null ? request.password() : null
        );
        return ResponseEntity.ok(Map.of(
                "tokenType", "Bearer",
                "accessToken", token
        ));
    }
}
