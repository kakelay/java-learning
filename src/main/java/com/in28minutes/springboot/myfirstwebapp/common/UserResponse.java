package com.in28minutes.springboot.myfirstwebapp.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMapping;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    
    private String cid;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String profilePic;
    private String coverPic;
    private String bio;
    private String website;
   
}