package com.in28minutes.springboot.myfirstwebapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogResponse {

    private Long id;
    private String tableName;
    private Long recordId;
    private String operation;
    private String oldValues;
    private String newValues;
    private String[] changedFields;
    private Long userId;
    private String username;
    private String ipAddress;
    private String userAgent;
    private String sessionId;
    private String transactionId;
    private LocalDateTime operationTimestamp;
}
