package com.in28minutes.springboot.myfirstwebapp.controller.audit;

import com.in28minutes.springboot.myfirstwebapp.common.BaseResponse;
import com.in28minutes.springboot.myfirstwebapp.common.RequestReference;
import com.in28minutes.springboot.myfirstwebapp.common.TraceLogger;
import com.in28minutes.springboot.myfirstwebapp.dto.response.AuditLogResponse;
import com.in28minutes.springboot.myfirstwebapp.entity.AuditLog;
import com.in28minutes.springboot.myfirstwebapp.repository.AuditLogRepository;
import io.micrometer.observation.annotation.Observed;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;
    private final MessageSource messageSource;
    private final TraceLogger traceLogger;

    public AuditLogController(AuditLogRepository auditLogRepository,
            MessageSource messageSource,
            TraceLogger traceLogger) {
        this.auditLogRepository = auditLogRepository;
        this.messageSource = messageSource;
        this.traceLogger = traceLogger;
    }

    private String generateReference() {
        return RequestReference.getOrCreate();
    }

    private AuditLogResponse toResponse(AuditLog auditLog) {
        return AuditLogResponse.builder()
                .id(auditLog.getId())
                .tableName(auditLog.getTableName())
                .recordId(auditLog.getRecordId())
                .operation(auditLog.getOperation())
                .oldValues(auditLog.getOldValues())
                .newValues(auditLog.getNewValues())
                .changedFields(auditLog.getChangedFields())
                .userId(auditLog.getUser() != null ? auditLog.getUser().getId() : null)
                .username(auditLog.getUsername())
                .ipAddress(auditLog.getIpAddress())
                .userAgent(auditLog.getUserAgent())
                .sessionId(auditLog.getSessionId())
                .transactionId(auditLog.getTransactionId())
                .operationTimestamp(auditLog.getOperationTimestamp())
                .build();
    }

    @GetMapping(value = "/logs", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "audit.getAll", contextualName = "get-all-audit-logs")
    public ResponseEntity<BaseResponse<List<AuditLogResponse>>> getAllAuditLogs() {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/audit/logs request");

        List<AuditLogResponse> logs = auditLogRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        String msg = messageSource.getMessage("response.success.message", null, "Audit logs retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, logs));
    }

    @GetMapping(value = "/logs/table/{tableName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "audit.getByTable", contextualName = "get-audit-by-table")
    public ResponseEntity<BaseResponse<List<AuditLogResponse>>> getAuditLogsByTable(@PathVariable String tableName) {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/audit/logs/table/" + tableName + " request");

        List<AuditLogResponse> logs = auditLogRepository.findByTableName(tableName).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        String msg = messageSource.getMessage("response.success.message", null, "Audit logs retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, logs));
    }

    @GetMapping(value = "/logs/record/{tableName}/{recordId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "audit.getByRecord", contextualName = "get-audit-by-record")
    public ResponseEntity<BaseResponse<List<AuditLogResponse>>> getAuditLogsByRecord(@PathVariable String tableName,
            @PathVariable Long recordId) {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/audit/logs/record/" + tableName + "/" + recordId + " request");

        List<AuditLogResponse> logs = auditLogRepository.findAuditTrailForRecord(tableName, recordId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        String msg = messageSource.getMessage("response.success.message", null, "Audit trail retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, logs));
    }

    @GetMapping(value = "/logs/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "audit.getByUser", contextualName = "get-audit-by-user")
    public ResponseEntity<BaseResponse<List<AuditLogResponse>>> getAuditLogsByUserId(@PathVariable Long userId) {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/audit/logs/user/" + userId + " request");

        List<AuditLogResponse> logs = auditLogRepository
                .findByUserId(userId, org.springframework.data.domain.PageRequest.of(0, 100)).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        String msg = messageSource.getMessage("response.success.message", null,
                "User audit logs retrieved successfully", LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, logs));
    }
}
