package com.in28minutes.springboot.myfirstwebapp.exception.business;

public class EmailAlreadyExistsException extends RuntimeException {
    private final String errorCode = "USR002";
    private final String email;

    public EmailAlreadyExistsException(String message) {
        super(message);
        this.email = message;
    }

    public String getErrorCode() {
        return errorCode;
    }
    public String getEmail(){
        return  email;
    }
}


