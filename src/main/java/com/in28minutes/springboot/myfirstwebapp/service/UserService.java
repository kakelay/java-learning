package com.in28minutes.springboot.myfirstwebapp.service;

import com.in28minutes.springboot.myfirstwebapp.dto.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponse getUser();
    Optional<UserResponse> getUserById(Long id);
    Optional<UserResponse> getUserByUsername(String username);
    Optional<UserResponse> getUserByEmail(String email);
    List<UserResponse> getAllUsers();
    List<UserResponse> getActiveUsers();
}