package com.in28minutes.springboot.myfirstwebapp.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ResponseInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ResponseInterceptor.class);
    private static final String RETRY_HEADER = "X-External-Api-Retry";

    private final AuthService authService;

    public ResponseInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);

        if (!shouldRetry(request, response)) {
            logger.info("External API response: {} {} -> {}", request.getMethod(), request.getURI(), response.getStatusCode());
            return response;
        }

        logger.warn("External API token rejected for {} {}; re-authenticating and retrying once",
                request.getMethod(), request.getURI());
        response.close();

        String token = authService.reAuthenticate();
        HttpRequest retryRequest = retryRequest(request, token);
        ClientHttpResponse retryResponse = execution.execute(retryRequest, body);
        logger.info("External API retry response: {} {} -> {}",
                request.getMethod(), request.getURI(), retryResponse.getStatusCode());
        return retryResponse;
    }

    private boolean shouldRetry(HttpRequest request, ClientHttpResponse response) throws IOException {
        if (request.getHeaders().containsKey(RETRY_HEADER)) {
            return false;
        }
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return true;
        }
        String responseBody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
        return responseBody.toLowerCase().contains("token expired");
    }

    private HttpRequest retryRequest(HttpRequest request, String token) {
        return new HttpRequestWrapper(request) {
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(super.getHeaders());
                headers.setBearerAuth(token);
                headers.set(RETRY_HEADER, "true");
                return headers;
            }
        };
    }
}
