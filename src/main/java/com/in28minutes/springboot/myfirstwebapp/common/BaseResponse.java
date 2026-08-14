package com.in28minutes.springboot.myfirstwebapp.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private String referenceNumber;
    private String responseCode;
    private String status;
    private String message;
    private T data;
    
    // Static factory methods for convenience
    public static <T> BaseResponse<T> success(String referenceNumber, String message, T data) {
        return BaseResponse.<T>builder()
                .referenceNumber(referenceNumber)
                .responseCode("00")
                .status("Success")
                .message(message)
                .data(data)
                .build();
    }
    
    public static <T> BaseResponse<T> error(String referenceNumber, String responseCode, String message) {
        return BaseResponse.<T>builder()
                .referenceNumber(referenceNumber)
                .responseCode(responseCode)
                .status("Fail")
                .message(message)
                .build();
    }
}