package com.in28minutes.springboot.myfirstwebapp.repository;

import com.in28minutes.springboot.myfirstwebapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByActiveTrue();

    List<User> findByLockedFalse();

    List<User> findByLastLoginDateBefore(LocalDateTime date);

    @Query("SELECT u FROM User u WHERE u.active = true AND u.locked = false")
    List<User> findActiveAndUnlockedUsers();

    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts >= :attempts")
    List<User> findUsersWithFailedAttempts(@Param("attempts") int attempts);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdDate >= :startDate")
    long countUsersCreatedAfter(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :roleName")
    List<User> findUsersByRoleName(@Param("roleName") String roleName);
}