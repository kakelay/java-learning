package com.in28minutes.springboot.myfirstwebapp.integration;

public class ApiClientException extends RuntimeException {

    public ApiClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
