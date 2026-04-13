package com.in28minutes.springboot.myfirstwebapp.controller;

import com.in28minutes.springboot.myfirstwebapp.common.BaseResponse;
import com.in28minutes.springboot.myfirstwebapp.common.UserResponse;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
public class UserController {
    
    private final MessageSource messageSource;
    
    public UserController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    private String generateReference() {
        return String.format("%010d", (long)(Math.random() * 1_000_000_000L));
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<UserResponse>> helloWorldInternationalized(Locale locale) {
        
        String ref = generateReference();
        
        String msg = messageSource.getMessage(
                "response.success.message",
                null,
                "Default Message",
                locale
        );
        
        UserResponse user = UserResponse.builder()
                .cid("USR001")
                .name("Elay Kak")
                .phone("010600261")
                .email("kakelay18052002@gmail.com")
                .address("123 Monivong Blvd")
                .city("Phnom Penh")
                .state("Phnom Penh")
                .country("Cambodia")
                .zipCode("12000")
                .profilePic("https://avatars.githubusercontent.com/u/110383694?v=4")
                .website("https://kakelay-dev.vercel.app/")
                .build();
        
        return ResponseEntity.ok(
                BaseResponse.success(ref, msg, user)
        );
    }
}