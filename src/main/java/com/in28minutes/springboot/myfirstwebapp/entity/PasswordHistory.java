package com.in28minutes.springboot.myfirstwebapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "changed_date")
    @Builder.Default
    private LocalDateTime changedDate = LocalDateTime.now();

    @Column(name = "changed_by", length = 50)
    private String changedBy;

    // Helper methods
    public boolean isRecent(int days) {
        return changedDate.isAfter(LocalDateTime.now().minusDays(days));
    }
}