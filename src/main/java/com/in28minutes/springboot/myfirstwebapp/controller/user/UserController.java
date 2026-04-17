package com.in28minutes.springboot.myfirstwebapp.controller.user;

import com.in28minutes.springboot.myfirstwebapp.common.BaseResponse;
import com.in28minutes.springboot.myfirstwebapp.dto.response.UserResponse;
import com.in28minutes.springboot.myfirstwebapp.context.LanguageContext;
import com.in28minutes.springboot.myfirstwebapp.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

@RestController
public class UserController {

    private final MessageSource messageSource;
    private final UserService userService;

    public UserController(MessageSource messageSource, UserService userService) {
        this.messageSource = messageSource;
        this.userService = userService;
    }

    private String generateReference() {
        return UUID.randomUUID().toString();
    }

    @GetMapping(value = "/user" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<UserResponse>> getUser() {

        String ref = generateReference();

        Locale locale = LanguageContext.getLocale();

        String msg = messageSource.getMessage(
                "response.success.message",
                null,
                "Default Message",
                locale
        );

        return ResponseEntity.ok(
                BaseResponse.success(ref, msg, userService.getUser())
        );
    }
}