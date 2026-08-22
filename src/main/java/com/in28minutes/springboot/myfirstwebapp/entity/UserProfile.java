package com.in28minutes.springboot.myfirstwebapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(unique = true, length = 20)
    private String cid; // Customer ID like T24

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "full_name", insertable = false, updatable = false)
    private String fullName; // Generated column

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 10)
    private String gender;

    @Column(name = "address_line1", length = 255)
    private String addressLine1;

    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Column(length = 20)
    private String phone;

    @Column(name = "alternate_phone", length = 20)
    private String alternatePhone;

    @Column(name = "profile_picture", length = 500)
    private String profilePicture;

    @Column(name = "cover_picture", length = 500)
    private String coverPicture;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 255)
    private String website;

    @Column(length = 100)
    private String occupation;

    @Column(length = 100)
    private String company;

    @Column(length = 100)
    private String nationality;

    @Column(name = "id_type", length = 50)
    private String idType; // Passport, National ID, etc.

    @Column(name = "id_number", length = 50)
    private String idNumber;

    @Column(name = "id_expiry_date")
    private LocalDate idExpiryDate;

    @Column(name = "marital_status", length = 20)
    private String maritalStatus;

    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @Column(name = "emergency_contact_relationship", length = 50)
    private String emergencyContactRelationship;

    @Column(name = "preferred_language", length = 10)
    @Builder.Default
    private String preferredLanguage = "en";

    @Column(length = 50)
    @Builder.Default
    private String timezone = "UTC";

    @Column(name = "is_profile_complete")
    @Builder.Default
    private Boolean profileComplete = true;


    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    // Helper methods
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        if (addressLine1 != null) address.append(addressLine1);
        if (addressLine2 != null) address.append(", ").append(addressLine2);
        if (city != null) address.append(", ").append(city);
        if (state != null) address.append(", ").append(state);
        if (zipCode != null) address.append(" ").append(zipCode);
        if (country != null) address.append(", ").append(country);
        return address.toString();
    }

    public boolean isProfileComplete() {
        return firstName != null && !firstName.trim().isEmpty() &&
               lastName != null && !lastName.trim().isEmpty() &&
               phone != null && !phone.trim().isEmpty();
    }
}