package com.in28minutes.springboot.myfirstwebapp.service.impl;

import com.in28minutes.springboot.myfirstwebapp.dto.response.UserResponse;
import com.in28minutes.springboot.myfirstwebapp.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserResponse getUser() {

        return UserResponse.builder()
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
    }
}