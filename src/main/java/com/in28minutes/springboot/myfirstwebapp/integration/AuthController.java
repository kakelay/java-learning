package com.in28minutes.springboot.myfirstwebapp.integration;

import com.in28minutes.springboot.myfirstwebapp.common.BaseResponse;
import com.in28minutes.springboot.myfirstwebapp.common.RequestReference;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MessageSource messageSource;

    public AuthController(AuthService authService, MessageSource messageSource) {
        this.authService = authService;
        this.messageSource = messageSource;
    }

    private String generateReference() {
        return RequestReference.getOrCreate();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponse<Map<String, String>>> signIn(
            @RequestHeader(value = "apiKey", required = false) String apiKey,
            @RequestHeader(value = "partnerId", required = false) String partnerId,
            @RequestHeader(value = "partnerid", required = false) String partnerIdLowercase,
            @RequestBody(required = false) SignInRequest request) {

        String ref = generateReference();

        String token = authService.issueLocalToken(
                apiKey,
                partnerId != null ? partnerId : partnerIdLowercase,
                request != null ? request.username() : null,
                request != null ? request.password() : null
        );

        String msg = messageSource.getMessage(
                "response.success.message",
                null,
                "Success",
                LocaleContextHolder.getLocale()
        );

        Map<String, String> data = Map.of(
                "tokenType", "Bearer",
                "accessToken", token
        );

        return ResponseEntity.ok(
                BaseResponse.success(ref, msg, data)
        );
    }
}