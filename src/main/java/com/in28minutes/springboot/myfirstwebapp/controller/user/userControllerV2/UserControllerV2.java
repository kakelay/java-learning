package com.in28minutes.springboot.myfirstwebapp.controller.user.userControllerV2;

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
@RequestMapping("/api/user/v2")
public class UserControllerV2 {

        private final MessageSource messageSource;
        private final UserService userService;
        private final TraceLogger traceLogger;

        public UserControllerV2(MessageSource messageSource, UserService userService, TraceLogger traceLogger) {
                this.messageSource = messageSource;
                this.userService = userService;
                this.traceLogger = traceLogger;
        }

        private String generateReference() {
                return UUID.randomUUID().toString();
        }

        @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Observed(name = "user.getById", contextualName = "get-user-by-id")
        public ResponseEntity<BaseResponse<UserResponse>> getUserById(@PathVariable Long id) {

                String ref = generateReference();

                traceLogger.logTrace("Processing /api/user/v2/" + id + " request");

                Optional<UserResponse> userResponse = userService.getUserById(id);

                if (userResponse.isPresent()) {
                        String msg = messageSource.getMessage(
                                        "response.success.message",
                                        null,
                                        "User found successfully",
                                        LocaleContextHolder.getLocale());
                        return ResponseEntity.ok(
                                        BaseResponse.success(ref, msg, userResponse.get()));
                } else {
                        String msg = messageSource.getMessage(
                                        "response.notfound.message",
                                        null,
                                        "User not found",
                                        LocaleContextHolder.getLocale());
                        return ResponseEntity.ok(
                                        BaseResponse.success(ref, msg, null));
                }
        }

        @GetMapping(value = "/activeUser", produces = MediaType.APPLICATION_JSON_VALUE)
        @Observed(name = "user.getActive", contextualName = "get-active-users")
        public ResponseEntity<BaseResponse<List<UserResponse>>> getActiveUsers() {

                String ref = generateReference();

                traceLogger.logTrace("Processing /api/user/v2/activeUser request");

                List<UserResponse> users = userService.getActiveUsers();

                String msg = messageSource.getMessage(
                                "response.success.message",
                                null,
                                "Active users retrieved successfully",
                                LocaleContextHolder.getLocale());

                return ResponseEntity.ok(
                                BaseResponse.success(ref, msg, users));
        }

}