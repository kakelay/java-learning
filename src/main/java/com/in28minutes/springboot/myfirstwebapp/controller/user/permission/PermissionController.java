package com.in28minutes.springboot.myfirstwebapp.controller.user.permission;

import com.in28minutes.springboot.myfirstwebapp.common.BaseResponse;
import com.in28minutes.springboot.myfirstwebapp.common.TraceLogger;
import com.in28minutes.springboot.myfirstwebapp.dto.response.PermissionResponse;
import com.in28minutes.springboot.myfirstwebapp.entity.Permission;
import com.in28minutes.springboot.myfirstwebapp.repository.PermissionRepository;
import io.micrometer.observation.annotation.Observed;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionRepository permissionRepository;
    private final MessageSource messageSource;
    private final TraceLogger traceLogger;

    public PermissionController(PermissionRepository permissionRepository,
            MessageSource messageSource,
            TraceLogger traceLogger) {
        this.permissionRepository = permissionRepository;
        this.messageSource = messageSource;
        this.traceLogger = traceLogger;
    }

    private String generateReference() {
        return UUID.randomUUID().toString();
    }

    private PermissionResponse toResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .permissionName(permission.getPermissionName())
                .permissionDescription(permission.getPermissionDescription())
                .resource(permission.getResource())
                .action(permission.getAction())
                .active(permission.getActive())
                .createdDate(permission.getCreatedDate())
                .build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "permission.getById", contextualName = "get-permission-by-id")
    public ResponseEntity<BaseResponse<PermissionResponse>> getPermissionById(@PathVariable Long id) {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/permissions/" + id + " request");

        Optional<Permission> permission = permissionRepository.findById(id);
        if (permission.isEmpty()) {
            String msg = messageSource.getMessage("response.notfound.message", null, "Permission not found",
                    LocaleContextHolder.getLocale());
            return ResponseEntity.ok(BaseResponse.success(ref, msg, null));
        }

        String msg = messageSource.getMessage("response.success.message", null, "Permission retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, toResponse(permission.get())));
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "permission.getAll", contextualName = "get-all-permissions")
    public ResponseEntity<BaseResponse<List<PermissionResponse>>> getAllPermissions() {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/permissions/all request");

        List<PermissionResponse> permissions = permissionRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        String msg = messageSource.getMessage("response.success.message", null, "Permissions retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, permissions));
    }

    @GetMapping(value = "/resource/{resource}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "permission.getByResource", contextualName = "get-permissions-by-resource")
    public ResponseEntity<BaseResponse<List<PermissionResponse>>> getPermissionsByResource(
            @PathVariable String resource) {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/permissions/resource/" + resource + " request");

        List<PermissionResponse> permissions = permissionRepository.findByResource(resource).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        String msg = messageSource.getMessage("response.success.message", null, "Permissions retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, permissions));
    }
}
