package com.in28minutes.springboot.myfirstwebapp.common;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TraceLogger {

    private static final Logger logger = LoggerFactory.getLogger(TraceLogger.class);

    private static final String MDC_REF = "ref";
    private static final String MDC_TRACE_ID = "traceId";
    private static final String MDC_SPAN_ID = "spanId";

    private final Tracer tracer;

    public TraceLogger(@Autowired(required = false) Tracer tracer) {
        this.tracer = tracer;
    }

    /** Call once per request (e.g. in the filter) to seed MDC for the whole call chain. */
    public String initTrace() {
        String referenceNumber = RequestReference.getOrCreate();
        MDC.put(MDC_REF, referenceNumber);

        Span currentSpan = (tracer != null) ? tracer.currentSpan() : null;
        if (currentSpan != null) {
            MDC.put(MDC_TRACE_ID, currentSpan.context().traceId());
            MDC.put(MDC_SPAN_ID, currentSpan.context().spanId());
        }
        return referenceNumber;
    }

    /** Call at the end of the request (finally block) to avoid leaking MDC across threads. */
    public void clearTrace() {
        MDC.remove(MDC_REF);
        MDC.remove(MDC_TRACE_ID);
        MDC.remove(MDC_SPAN_ID);
    }

    public void logTrace(String context, Object... kvArgs) {
        logger.info(buildMessage(context, kvArgs));
    }

    public void logWarn(String context, Object... kvArgs) {
        logger.warn(buildMessage(context, kvArgs));
    }

    public void logError(String context, Throwable t, Object... kvArgs) {
        logger.error(buildMessage(context, kvArgs), t);
    }

    /** Builds "context | k1=v1 | k2=v2" from alternating key/value pairs. */
    private String buildMessage(String context, Object... kvArgs) {
        StringBuilder sb = new StringBuilder(context);
        for (int i = 0; i + 1 < kvArgs.length; i += 2) {
            sb.append(" | ").append(kvArgs[i]).append("=").append(kvArgs[i + 1]);
        }
        return sb.toString();
    }
}