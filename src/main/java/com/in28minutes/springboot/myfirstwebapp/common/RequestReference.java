package com.in28minutes.springboot.myfirstwebapp.common;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

public final class RequestReference {

    public static final String ATTRIBUTE_NAME = "referenceNumber";
    public static final String MDC_KEY = "referenceNumber";

    private RequestReference() {
    }

    public static String bind(HttpServletRequest request) {
        Object existing = request.getAttribute(ATTRIBUTE_NAME);
        if (existing instanceof String referenceNumber && !referenceNumber.isBlank()) {
            MDC.put(MDC_KEY, referenceNumber);
            return referenceNumber;
        }

        String referenceNumber = UUID.randomUUID().toString();
        request.setAttribute(ATTRIBUTE_NAME, referenceNumber);
        MDC.put(MDC_KEY, referenceNumber);
        return referenceNumber;
    }

    public static String getOrCreate() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return UUID.randomUUID().toString();
        }

        return bind(attributes.getRequest());
    }
}
