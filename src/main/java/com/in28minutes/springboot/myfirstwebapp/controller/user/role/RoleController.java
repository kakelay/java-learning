package com.in28minutes.springboot.myfirstwebapp.controller.user.role;

import com.in28minutes.springboot.myfirstwebapp.common.BaseResponse;
import com.in28minutes.springboot.myfirstwebapp.common.TraceLogger;
import com.in28minutes.springboot.myfirstwebapp.dto.response.RoleResponse;
import com.in28minutes.springboot.myfirstwebapp.entity.Role;
import com.in28minutes.springboot.myfirstwebapp.repository.RoleRepository;
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
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleRepository roleRepository;
    private final MessageSource messageSource;
    private final TraceLogger traceLogger;

    public RoleController(RoleRepository roleRepository,
            MessageSource messageSource,
            TraceLogger traceLogger) {
        this.roleRepository = roleRepository;
        this.messageSource = messageSource;
        this.traceLogger = traceLogger;
    }

    private String generateReference() {
        return UUID.randomUUID().toString();
    }

    private RoleResponse toResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .roleDescription(role.getRoleDescription())
                .active(role.getActive())
                .createdDate(role.getCreatedDate())
                .updatedDate(role.getUpdatedDate())
                .createdBy(role.getCreatedBy())
                .updatedBy(role.getUpdatedBy())
                .build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "role.getById", contextualName = "get-role-by-id")
    public ResponseEntity<BaseResponse<RoleResponse>> getRoleById(@PathVariable Long id) {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/roles/" + id + " request");

        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            String msg = messageSource.getMessage("response.notfound.message", null, "Role not found",
                    LocaleContextHolder.getLocale());
            return ResponseEntity.ok(BaseResponse.success(ref, msg, null));
        }

        String msg = messageSource.getMessage("response.success.message", null, "Role retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, toResponse(role.get())));
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "role.getAll", contextualName = "get-all-roles")
    public ResponseEntity<BaseResponse<List<RoleResponse>>> getAllRoles() {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/roles/all request");

        List<RoleResponse> roles = roleRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        String msg = messageSource.getMessage("response.success.message", null, "Roles retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, roles));
    }

    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "role.getActive", contextualName = "get-active-roles")
    public ResponseEntity<BaseResponse<List<RoleResponse>>> getActiveRoles() {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/roles/active request");

        List<RoleResponse> roles = roleRepository.findActiveRolesOrdered().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        String msg = messageSource.getMessage("response.success.message", null, "Active roles retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, roles));
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Observed(name = "role.getByUserId", contextualName = "get-roles-by-user-id")
    public ResponseEntity<BaseResponse<List<RoleResponse>>> getRolesByUserId(@PathVariable Long userId) {
        String ref = generateReference();
        traceLogger.logTrace("Processing /api/roles/user/" + userId + " request");

        List<RoleResponse> roles = roleRepository.findRolesByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        String msg = messageSource.getMessage("response.success.message", null, "Roles retrieved successfully",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(BaseResponse.success(ref, msg, roles));
    }
}
