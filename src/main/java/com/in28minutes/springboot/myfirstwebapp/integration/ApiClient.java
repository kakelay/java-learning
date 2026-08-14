package com.in28minutes.springboot.myfirstwebapp.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    private final RestTemplate authenticatedRestTemplate;
    private final ExternalApiProperties properties;

    public ApiClient(RestTemplate authenticatedRestTemplate, ExternalApiProperties properties) {
        this.authenticatedRestTemplate = authenticatedRestTemplate;
        this.properties = properties;
    }

    public <T> ResponseEntity<T> get(String path, Class<T> responseType) {
        return exchange(path, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> post(String path, Object requestBody, Class<T> responseType) {
        return exchange(path, HttpMethod.POST, requestBody, responseType);
    }

    public <T> ResponseEntity<T> put(String path, Object requestBody, Class<T> responseType) {
        return exchange(path, HttpMethod.PUT, requestBody, responseType);
    }

    public <T> ResponseEntity<T> delete(String path, Class<T> responseType) {
        return exchange(path, HttpMethod.DELETE, null, responseType);
    }

    public <T> ResponseEntity<T> exchange(String path,
                                          HttpMethod method,
                                          Object requestBody,
                                          Class<T> responseType) {
        try {
            return authenticatedRestTemplate.exchange(
                    properties.apiUrl(path),
                    method,
                    entity(requestBody),
                    responseType
            );
        } catch (RestClientException exception) {
            logger.error("External API call failed: {} {} - {}", method, path, exception.getMessage());
            throw new ApiClientException("External API call failed", exception);
        }
    }

    public <T> ResponseEntity<T> exchange(String path,
                                          HttpMethod method,
                                          Object requestBody,
                                          ParameterizedTypeReference<T> responseType) {
        try {
            return authenticatedRestTemplate.exchange(
                    properties.apiUrl(path),
                    method,
                    entity(requestBody),
                    responseType
            );
        } catch (RestClientException exception) {
            logger.error("External API call failed: {} {} - {}", method, path, exception.getMessage());
            throw new ApiClientException("External API call failed", exception);
        }
    }

    private HttpEntity<Object> entity(Object requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(requestBody, headers);
    }
}
