package com.in28minutes.springboot.myfirstwebapp.controller.user;

import com.in28minutes.springboot.myfirstwebapp.common.BaseResponse;
import com.in28minutes.springboot.myfirstwebapp.common.TraceLogger;
import com.in28minutes.springboot.myfirstwebapp.dto.response.UserResponse;
import com.in28minutes.springboot.myfirstwebapp.service.UserService;
import io.micrometer.observation.annotation.Observed;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    private final MessageSource messageSource;
    private final UserService userService;
    private final TraceLogger traceLogger;

    public UserController(MessageSource messageSource, UserService userService, TraceLogger traceLogger) {
        this.messageSource = messageSource;
        this.userService = userService;
        this.traceLogger = traceLogger;
    }

    private String generateReference() {
        return UUID.randomUUID().toString();
    }

    @GetMapping(value = "/v1/user" , produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "user.get", contextualName = "get-user")
    public ResponseEntity<BaseResponse<UserResponse>> getUser() {

        String ref = generateReference();
        
        traceLogger.logTrace("Processing /v1/user request");
        
        String msg = messageSource.getMessage(
                "response.success.message",
                null,
                "Default Message",
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.ok(
                BaseResponse.success(ref, msg, userService.getUser())
        );
    }
}