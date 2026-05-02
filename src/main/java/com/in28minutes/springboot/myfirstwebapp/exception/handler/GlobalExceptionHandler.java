package com.in28minutes.springboot.myfirstwebapp.exception.handler;

import com.in28minutes.springboot.myfirstwebapp.common.BaseResponse;
import com.in28minutes.springboot.myfirstwebapp.exception.business.EmailAlreadyExistsException;
import com.in28minutes.springboot.myfirstwebapp.exception.business.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(@Qualifier("messageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<BaseResponse<Object>> handleUserExists(UserAlreadyExistsException ex) {

        String ref = UUID.randomUUID().toString();

        String message = messageSource.getMessage(
                "user.exists.username",
                new Object[]{ex.getUsername()},
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.badRequest()
                .body(BaseResponse.error(
                        ref,
                        ex.getErrorCode(),
                        message
                ));
    }

    // ⭐ Email exists (if you created it)
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<BaseResponse<Object>> handleEmailExists(EmailAlreadyExistsException ex) {

        String ref = UUID.randomUUID().toString();

        String message = messageSource.getMessage(
                "user.exists.email",
                new Object[]{ex.getEmail()},
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.badRequest()
                .body(BaseResponse.error(
                        ref,
                        ex.getErrorCode(),
                        message
                ));
    }

    // ⭐ Validation error (@Valid)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {

        String ref = UUID.randomUUID().toString();

        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return ResponseEntity.badRequest()
                .body(BaseResponse.error(ref, "VAL001", errorMsg));
    }

    // ⭐ Fallback (VERY IMPORTANT)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGeneric(Exception ex) {

        String ref = UUID.randomUUID().toString();

        return ResponseEntity.internalServerError()
                .body(BaseResponse.error(
                        ref,
                        "SYS001",
                        "Internal server error"
                ));
    }
}