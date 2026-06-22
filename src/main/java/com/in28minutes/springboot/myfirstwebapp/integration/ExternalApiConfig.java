package com.in28minutes.springboot.myfirstwebapp.integration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@EnableConfigurationProperties(ExternalApiProperties.class)
public class ExternalApiConfig {

    @Bean
    public RestTemplate loginRestTemplate(RestTemplateBuilder builder, ExternalApiProperties properties) {
        return builder
                .requestFactory(() -> requestFactory(properties))
                .errorHandler(new DefaultResponseErrorHandler())
                .build();
    }

    @Bean
    public RestTemplate authenticatedRestTemplate(RestTemplateBuilder builder,
                                                  ExternalApiProperties properties,
                                                  RequestInterceptor requestInterceptor,
                                                  ResponseInterceptor responseInterceptor) {
        return builder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(requestFactory(properties)))
                .interceptors(List.of(requestInterceptor, responseInterceptor))
                .errorHandler(new DefaultResponseErrorHandler())
                .build();
    }

    private ClientHttpRequestFactory requestFactory(ExternalApiProperties properties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        return factory;
    }
}
