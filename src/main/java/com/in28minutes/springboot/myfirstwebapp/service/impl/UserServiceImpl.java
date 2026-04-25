package com.in28minutes.springboot.myfirstwebapp.service.impl;

import com.in28minutes.springboot.myfirstwebapp.dto.response.UserResponse;
import com.in28minutes.springboot.myfirstwebapp.entity.User;
import com.in28minutes.springboot.myfirstwebapp.repository.UserRepository;
import com.in28minutes.springboot.myfirstwebapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getUser() {
        // Return the first active user as default, or create a default response
        return userRepository.findByActiveTrue().stream()
                .findFirst()
                .map(this::convertToUserResponse)
                .orElseGet(this::getDefaultUserResponse);
    }

    @Override
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToUserResponse);
    }

    @Override
    public Optional<UserResponse> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToUserResponse);
    }

    @Override
    public Optional<UserResponse> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToUserResponse);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getActiveUsers() {
        return userRepository.findByActiveTrue().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse.UserResponseBuilder builder = UserResponse.builder()
                .cid(user.getId().toString())
                .name(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail());

        if (user.getProfile() != null) {
            builder
                .address(user.getProfile().getAddressLine1())
                .city(user.getProfile().getCity())
                .state(user.getProfile().getState())
                .country(user.getProfile().getCountry())
                .zipCode(user.getProfile().getZipCode())
                .bio(user.getProfile().getBio())
                .website(user.getProfile().getWebsite());
        }

        return builder.build();
    }

    private UserResponse getDefaultUserResponse() {
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