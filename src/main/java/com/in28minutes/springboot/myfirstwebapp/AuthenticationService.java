package com.in28minutes.springboot.myfirstwebapp;


import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    //authentication logic
    //return true if authenticated else false
    public boolean authenticate( String userName, String password ) {
        boolean isValidUserName = userName.equalsIgnoreCase( "admin" );
        boolean isValidPassword = password.equalsIgnoreCase( "123" );
        return isValidUserName && isValidPassword;
    }
    
}
