package com.in28minutes.springboot.myfirstwebapp.controller.user;

import com.in28minutes.springboot.myfirstwebapp.common.BaseResponse;
import com.in28minutes.springboot.myfirstwebapp.dto.response.UserResponse;
import com.in28minutes.springboot.myfirstwebapp.service.UserService;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final MessageSource messageSource;
    private final UserService userService;
    private final Tracer tracer;

    public UserController(MessageSource messageSource, UserService userService, @Autowired(required = false) Tracer tracer) {
        this.messageSource = messageSource;
        this.userService = userService;
        this.tracer = tracer;
    }

    private String generateReference() {
        return UUID.randomUUID().toString();
    }

    @GetMapping(value = "/user" , produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "user.get", contextualName = "get-user")
    public ResponseEntity<BaseResponse<UserResponse>> getUser() {

        String ref = generateReference();

        if (tracer != null && tracer.currentSpan() != null) {
            logger.info("Processing user request with traceId: {}", tracer.currentSpan().context().traceId());
        } else {
            logger.info("Processing user request (no tracing available)");
        }

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