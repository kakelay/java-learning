package com.in28minutes.springboot.myfirstwebapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name", nullable = false, length = 100)
    private String tableName;

    @Column(name = "record_id")
    private Long recordId;

    @Column(nullable = false, length = 20)
    private String operation; // INSERT, UPDATE, DELETE

    @Column(name = "old_values", columnDefinition = "JSONB")
    private String oldValues; // JSON string

    @Column(name = "new_values", columnDefinition = "JSONB")
    private String newValues; // JSON string

    @Column(name = "changed_fields", columnDefinition = "TEXT[]")
    private String[] changedFields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 50)
    private String username;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "session_id", length = 255)
    private String sessionId;

    @Column(name = "transaction_id", length = 255)
    private String transactionId;

    @Column(name = "operation_timestamp")
    @Builder.Default
    private LocalDateTime operationTimestamp = LocalDateTime.now();

    // Helper methods
    public String getOperationDescription() {
        return String.format("%s operation on %s (ID: %s) by user %s at %s",
            operation, tableName, recordId, username, operationTimestamp);
    }
}