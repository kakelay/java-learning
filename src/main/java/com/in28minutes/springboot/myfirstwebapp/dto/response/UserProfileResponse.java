package com.in28minutes.springboot.myfirstwebapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileResponse {

    private Long id;
    private Long userId;
    private String cid;
    private String firstName;
    private String lastName;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String phone;
    private String alternatePhone;
    private String profilePicture;
    private String coverPicture;
    private String bio;
    private String website;
    private String occupation;
    private String company;
    private String nationality;
    private String idType;
    private String idNumber;
    private LocalDate idExpiryDate;
    private String maritalStatus;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelationship;
    private String preferredLanguage;
    private String timezone;
    private Boolean profileComplete;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
