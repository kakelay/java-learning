package com.in28minutes.springboot.myfirstwebapp.repository;

import com.in28minutes.springboot.myfirstwebapp.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserId(Long userId);

    Optional<UserProfile> findByCid(String cid);

    List<UserProfile> findByCity(String city);

    List<UserProfile> findByState(String state);

    List<UserProfile> findByCountry(String country);

    @Query("SELECT p FROM UserProfile p WHERE p.firstName LIKE %:name% OR p.lastName LIKE %:name%")
    List<UserProfile> findByNameContaining(@Param("name") String name);

    @Query("SELECT p FROM UserProfile p WHERE p.profileComplete = true OR p.profileComplete = false ")
    List<UserProfile> findIncompleteProfiles();

    @Query("SELECT COUNT(p) FROM UserProfile p WHERE p.country = :country")
    long countByCountry(@Param("country") String country);

    @Query("SELECT p FROM UserProfile p WHERE p.phone = :phone OR p.alternatePhone = :phone")
    List<UserProfile> findByPhoneNumber(@Param("phone") String phone);
}