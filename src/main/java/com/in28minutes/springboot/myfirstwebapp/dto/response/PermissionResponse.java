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
public class PermissionResponse {

    private Long id;
    private String permissionName;
    private String permissionDescription;
    private String resource;
    private String action;
    private Boolean active;
    private LocalDateTime createdDate;
}
