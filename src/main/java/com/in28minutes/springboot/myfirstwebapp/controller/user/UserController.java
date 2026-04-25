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

import java.util.List;
import java.util.Optional;
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

    @GetMapping(value = "/v1/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "user.getById", contextualName = "get-user-by-id")
    public ResponseEntity<BaseResponse<UserResponse>> getUserById(@PathVariable Long id) {

        String ref = generateReference();
        
        traceLogger.logTrace("Processing /v1/user/" + id + " request");
        
        Optional<UserResponse> userResponse = userService.getUserById(id);
        
        if (userResponse.isPresent()) {
            String msg = messageSource.getMessage(
                    "response.success.message",
                    null,
                    "User found successfully",
                    LocaleContextHolder.getLocale()
            );
            return ResponseEntity.ok(
                    BaseResponse.success(ref, msg, userResponse.get())
            );
        } else {
            String msg = messageSource.getMessage(
                    "response.notfound.message",
                    null,
                    "User not found",
                    LocaleContextHolder.getLocale()
            );
            return ResponseEntity.ok(
                    BaseResponse.success(ref, msg, null)
            );
        }
    }

    @GetMapping(value = "/v1/user/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "user.getByUsername", contextualName = "get-user-by-username")
    public ResponseEntity<BaseResponse<UserResponse>> getUserByUsername(@PathVariable String username) {

        String ref = generateReference();
        
        traceLogger.logTrace("Processing /v1/user/username/" + username + " request");
        
        Optional<UserResponse> userResponse = userService.getUserByUsername(username);
        
        if (userResponse.isPresent()) {
            String msg = messageSource.getMessage(
                    "response.success.message",
                    null,
                    "User found successfully",
                    LocaleContextHolder.getLocale()
            );
            return ResponseEntity.ok(
                    BaseResponse.success(ref, msg, userResponse.get())
            );
        } else {
            String msg = messageSource.getMessage(
                    "response.notfound.message",
                    null,
                    "User not found",
                    LocaleContextHolder.getLocale()
            );
            return ResponseEntity.ok(
                    BaseResponse.success(ref, msg, null)
            );
        }
    }

    @GetMapping(value = "/v1/user/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "user.getByEmail", contextualName = "get-user-by-email")
    public ResponseEntity<BaseResponse<UserResponse>> getUserByEmail(@PathVariable String email) {

        String ref = generateReference();
        
        traceLogger.logTrace("Processing /v1/user/email/" + email + " request");
        
        Optional<UserResponse> userResponse = userService.getUserByEmail(email);
        
        if (userResponse.isPresent()) {
            String msg = messageSource.getMessage(
                    "response.success.message",
                    null,
                    "User found successfully",
                    LocaleContextHolder.getLocale()
            );
            return ResponseEntity.ok(
                    BaseResponse.success(ref, msg, userResponse.get())
            );
        } else {
            String msg = messageSource.getMessage(
                    "response.notfound.message",
                    null,
                    "User not found",
                    LocaleContextHolder.getLocale()
            );
            return ResponseEntity.ok(
                    BaseResponse.success(ref, msg, null)
            );
        }
    }

    @GetMapping(value = "/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "user.getAll", contextualName = "get-all-users")
    public ResponseEntity<BaseResponse<List<UserResponse>>> getAllUsers() {

        String ref = generateReference();
        
        traceLogger.logTrace("Processing /v1/users request");
        
        List<UserResponse> users = userService.getAllUsers();
        
        String msg = messageSource.getMessage(
                "response.success.message",
                null,
                "Users retrieved successfully",
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.ok(
                BaseResponse.success(ref, msg, users)
        );
    }

    @GetMapping(value = "/v1/users/active", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "user.getActive", contextualName = "get-active-users")
    public ResponseEntity<BaseResponse<List<UserResponse>>> getActiveUsers() {

        String ref = generateReference();
        
        traceLogger.logTrace("Processing /v1/users/active request");
        
        List<UserResponse> users = userService.getActiveUsers();
        
        String msg = messageSource.getMessage(
                "response.success.message",
                null,
                "Active users retrieved successfully",
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.ok(
                BaseResponse.success(ref, msg, users)
        );
    }
}