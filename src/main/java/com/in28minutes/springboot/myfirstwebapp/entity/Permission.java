package com.in28minutes.springboot.myfirstwebapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission_name", unique = true, nullable = false, length = 100)
    private String permissionName;

    @Column(name = "permission_description", length = 255)
    private String permissionDescription;

    @Column(nullable = false, length = 100)
    private String resource; // e.g., 'USER', 'ACCOUNT', 'TRANSACTION'

    @Column(nullable = false, length = 50)
    private String action; // e.g., 'CREATE', 'READ', 'UPDATE', 'DELETE'

    @Column(name = "is_active")
    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    // Relationships
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    // Helper methods
    public String getFullPermission() {
        return resource + "_" + action;
    }
}