package com.in28minutes.springboot.myfirstwebapp.exception.business;

public class UserAlreadyExistsException extends RuntimeException {

    private final String errorCode = "USR001";
    private final String username;

    public UserAlreadyExistsException(String username) {
        super(username);
        this.username = username;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getUsername() {
        return username;
    }



}
