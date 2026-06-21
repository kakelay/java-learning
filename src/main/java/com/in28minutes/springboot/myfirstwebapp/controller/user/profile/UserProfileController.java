package com.in28minutes.springboot.myfirstwebapp.controller.user.profile;

import com.in28minutes.springboot.myfirstwebapp.common.BaseResponse;
import com.in28minutes.springboot.myfirstwebapp.common.TraceLogger;
import com.in28minutes.springboot.myfirstwebapp.dto.response.UserProfileResponse;
import com.in28minutes.springboot.myfirstwebapp.entity.UserProfile;
import com.in28minutes.springboot.myfirstwebapp.repository.UserProfileRepository;
import io.micrometer.observation.annotation.Observed;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {

    private final UserProfileRepository userProfileRepository;
    private final MessageSource messageSource;
    private final TraceLogger traceLogger;

    public UserProfileController(UserProfileRepository userProfileRepository,
            MessageSource messageSource,
            TraceLogger traceLogger) {
        this.userProfileRepository = userProfileRepository;
        this.messageSource = messageSource;
        this.traceLogger = traceLogger;
    }

    private String generateReference() {
        return UUID.randomUUID().toString();
    }

    private UserProfileResponse toResponse(UserProfile profile) {
        return UserProfileResponse.builder()
                .id(profile.getId())
                .cid(profile.getUser() != null ? profile.getUser().getId().toString() : "")
//                .cid(profile.getCid())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .fullName(profile.getFullName())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .addressLine1(profile.getAddressLine1())
                .addressLine2(profile.getAddressLine2())
                .city(profile.getCity())
                .state(profile.getState())
                .country(profile.getCountry())
                .zipCode(profile.getZipCode())
                .phone(profile.getPhone())
                .alternatePhone(profile.getAlternatePhone())
                .profilePicture(profile.getProfilePicture())
                .coverPicture(profile.getCoverPicture())
                .bio(profile.getBio())
                .website(profile.getWebsite())
                .occupation(profile.getOccupation())
                .company(profile.getCompany())
                .nationality(profile.getNationality())
                .idType(profile.getIdType())
                .idNumber(profile.getIdNumber())
                .idExpiryDate(profile.getIdExpiryDate())
                .maritalStatus(profile.getMaritalStatus())
                .emergencyContactName(profile.getEmergencyContactName())
                .emergencyContactPhone(profile.getEmergencyContactPhone())
                .emergencyContactRelationship(profile.getEmergencyContactRelationship())
                .preferredLanguage(profile.getPreferredLanguage())
                .timezone(profile.getTimezone())
                .profileComplete(profile.isProfileComplete())
                .createdDate(profile.getCreatedDate())
                .updatedDate(profile.getUpdatedDate())
                .build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "profile.getById", contextualName = "get-profile-by-id")
    public ResponseEntity<BaseResponse<UserProfileResponse>> getProfileById(@PathVariable Long id) {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/user-profile/" + id + " request");

        Optional<UserProfile> profile = userProfileRepository.findById(id);
        if (profile.isEmpty()) {
            String msg = messageSource.getMessage("response.notfound.message", null, "Profile not found",
                    LocaleContextHolder.getLocale());
            return ResponseEntity.ok(BaseResponse.success(ref, msg, null));
        }

        String msg = messageSource.getMessage("response.success.message", null, "Profile retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, toResponse(profile.get())));
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "profile.getByUserId", contextualName = "get-profile-by-user-id")
    public ResponseEntity<BaseResponse<UserProfileResponse>> getProfileByUserId(@PathVariable Long userId) {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/user-profile/user/" + userId + " request");

        Optional<UserProfile> profile = userProfileRepository.findByUserId(userId);
        if (profile.isEmpty()) {
            String msg = messageSource.getMessage("response.notfound.message", null, "Profile not found",
                    LocaleContextHolder.getLocale());
            return ResponseEntity.ok(BaseResponse.success(ref, msg, null));
        }

        String msg = messageSource.getMessage("response.success.message", null, "Profile retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, toResponse(profile.get())));
    }

    @GetMapping(value = "/cid/{cid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "profile.getByCid", contextualName = "get-profile-by-cid")
    public ResponseEntity<BaseResponse<UserProfileResponse>> getProfileByCid(@PathVariable String cid) {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/user-profile/cid/" + cid + " request");

        Optional<UserProfile> profile = userProfileRepository.findByCid(cid);
        if (profile.isEmpty()) {
            String msg = messageSource.getMessage("response.notfound.message", null, "Profile not found",
                    LocaleContextHolder.getLocale());
            return ResponseEntity.ok(BaseResponse.success(ref, msg, null));
        }

        String msg = messageSource.getMessage("response.success.message", null, "Profile retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, toResponse(profile.get())));
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "profile.searchByName", contextualName = "search-profiles-by-name")
    public ResponseEntity<BaseResponse<List<UserProfileResponse>>> searchProfilesByName(@RequestParam String name) {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/user-profile/search?name=" + name + " request");

        List<UserProfileResponse> profiles = userProfileRepository.findByNameContaining(name).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        String msg = messageSource.getMessage("response.success.message", null, "Profiles retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, profiles));
    }

    @GetMapping(value = "/incomplete", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "profile.getIncomplete", contextualName = "get-incomplete-profiles")
    public ResponseEntity<BaseResponse<List<UserProfileResponse>>> getIncompleteProfiles() {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/user-profile/incomplete request");

        List<UserProfileResponse> profiles = userProfileRepository.findIncompleteProfiles().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        String msg = messageSource.getMessage("response.success.message", null,
                "Incomplete profiles retrieved successfully", LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, profiles));
    }
}
