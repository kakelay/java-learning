package com.in28minutes.springboot.myfirstwebapp.service.impl;

import com.in28minutes.springboot.myfirstwebapp.dto.request.CreateUserRequest;
import com.in28minutes.springboot.myfirstwebapp.dto.request.UpdateUserRequest;
import com.in28minutes.springboot.myfirstwebapp.dto.response.UserResponse;
import com.in28minutes.springboot.myfirstwebapp.entity.User;
import com.in28minutes.springboot.myfirstwebapp.entity.UserPreferences;
import com.in28minutes.springboot.myfirstwebapp.entity.UserProfile;
import com.in28minutes.springboot.myfirstwebapp.repository.UserRepository;
import com.in28minutes.springboot.myfirstwebapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // Assuming passwordEncoder is injected
                .email(request.getEmail())
                .phone(request.getPhone())
                .active(true)
                .locked(false)
                .failedLoginAttempts(0)
                .passwordChangedDate(LocalDateTime.now())
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .build();

        // Create profile if profile data is provided
        if (request.getFirstName() != null || request.getLastName() != null ||
            request.getAddressLine1() != null || request.getCity() != null) {
            UserProfile profile = UserProfile.builder()
                    .user(user)
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .addressLine1(request.getAddressLine1())
                    .addressLine2(request.getAddressLine2())
                    .city(request.getCity())
                    .state(request.getState())
                    .country(request.getCountry())
                    .zipCode(request.getZipCode())
                    .bio(request.getBio())
                    .website(request.getWebsite())
                    .build();
            user.setProfile(profile);
        }

        // Create preferences if preference data is provided
        if (request.getTheme() != null || request.getLanguage() != null || request.getTimezone() != null) {
            UserPreferences preferences = UserPreferences.builder()
                    .user(user)
                    .theme(request.getTheme() != null ? request.getTheme() : "light")
                    .language(request.getLanguage() != null ? request.getLanguage() : "en")
                    .timezone(request.getTimezone() != null ? request.getTimezone() : "UTC")
                    .build();
            user.setPreferences(preferences);
        }

        User savedUser = userRepository.save(user);
        return convertToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public Optional<UserResponse> updateUser(Long id, UpdateUserRequest request) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();

        // Check for username/email conflicts if they're being updated
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Username already exists: " + request.getUsername());
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        // Update basic fields
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPasswordChangedDate(LocalDateTime.now());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }
        if (request.getLocked() != null) {
            user.setLocked(request.getLocked());
        }

        user.setUpdatedBy("SYSTEM");
        user.setUpdatedDate(LocalDateTime.now());

        // Update profile
        if (user.getProfile() == null) {
            user.setProfile(new UserProfile());
            user.getProfile().setUser(user);
        }
        UserProfile profile = user.getProfile();
        if (request.getFirstName() != null) profile.setFirstName(request.getFirstName());
        if (request.getLastName() != null) profile.setLastName(request.getLastName());
        if (request.getAddressLine1() != null) profile.setAddressLine1(request.getAddressLine1());
        if (request.getAddressLine2() != null) profile.setAddressLine2(request.getAddressLine2());
        if (request.getCity() != null) profile.setCity(request.getCity());
        if (request.getState() != null) profile.setState(request.getState());
        if (request.getCountry() != null) profile.setCountry(request.getCountry());
        if (request.getZipCode() != null) profile.setZipCode(request.getZipCode());
        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getWebsite() != null) profile.setWebsite(request.getWebsite());

        // Update preferences
        if (user.getPreferences() == null) {
            user.setPreferences(new UserPreferences());
            user.getPreferences().setUser(user);
        }
        UserPreferences preferences = user.getPreferences();
        if (request.getTheme() != null) preferences.setTheme(request.getTheme());
        if (request.getLanguage() != null) preferences.setLanguage(request.getLanguage());
        if (request.getTimezone() != null) preferences.setTimezone(request.getTimezone());

        User savedUser = userRepository.save(user);
        return Optional.of(convertToUserResponse(savedUser));
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