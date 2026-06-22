package com.in28minutes.springboot.myfirstwebapp.common;

import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TraceLogger {

    private static final Logger logger = LoggerFactory.getLogger(TraceLogger.class);
    private final Tracer tracer;

    public TraceLogger(@Autowired(required = false) Tracer tracer) {
        this.tracer = tracer;
    }

    public void logTrace(String context) {
        String referenceNumber = RequestReference.getOrCreate();
        if (tracer != null && tracer.currentSpan() != null) {
            logger.info("{} | referenceNumber={} | traceId={}", context, referenceNumber,
                    Objects.requireNonNull(tracer.currentSpan()).context().traceId());
        } else {
            logger.info("{} | referenceNumber={} | traceId=not-available", context, referenceNumber);
        }
    }
}
