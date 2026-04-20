package com.in28minutes.springboot.myfirstwebapp.controller.versioning;

import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class VersioningPersonController {

    private static final Logger logger = LoggerFactory.getLogger(VersioningPersonController.class);

    private final Tracer tracer;

    public VersioningPersonController(@Autowired(required = false) Tracer tracer) {
        this.tracer = tracer;
    }

    private void logTrace(String context) {
        if (tracer != null && tracer.currentSpan() != null) {
            logger.info("{} with traceId: {}", context, Objects.requireNonNull(tracer.currentSpan()).context().traceId());
        } else {
            logger.info("{} (no tracing available)", context);
        }
    }

    @GetMapping("/v1/person")
    public PersonV1 getFirstVersionOfPerson(){
        logTrace("Processing /v1/person request");
        return new PersonV1("Kak Elay");
    }

    @GetMapping("/v2/person")
    public PersonV2 getSecondVersionOfPerson(){
        logTrace("Processing /v2/person request");
        return new PersonV2(new Name ("Kruy" , "Tharin"));
    }

    @GetMapping(path = "/person",params = "version=1")
    public PersonV2 getFirstVersionOfPersonRequestParameter(){
        logTrace("Processing /person?version=1 request");
        return new PersonV2(new Name ("Kak" , "MengHour"));
    }

    @GetMapping(path = "/person",params = "version=2")
    public PersonV2 getSecondVersionOfPersonRequestParameter(){
        logTrace("Processing /person?version=2 request");
        return new PersonV2(new Name ("Ly" , "MengNgounn"));
    }

    @GetMapping(path = "/person/header", headers = "X-API-VERSION=1")
    public PersonV1 getFirstVersionOfPersonRequestHeader(){
        logTrace("Processing /person/header header v1 request");
        return new PersonV1("MAK LIN1");
    }

    @GetMapping(path = "/person/header", headers = "X-API-VERSION=2")
    public PersonV1 getSecondVersionOfPersonRequestHeader(){
        logTrace("Processing /person/header header v2 request");
        return new PersonV1("MAK LIN2");
    }

}
