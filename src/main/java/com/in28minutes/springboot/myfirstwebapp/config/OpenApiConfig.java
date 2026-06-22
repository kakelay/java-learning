package com.in28minutes.springboot.myfirstwebapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI myFirstWebappOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My First Webapp API")
                        .version("1.0.0")
                        .description("User, role, permission, audit, and authentication APIs. Use /auth/sign-in to get a 1-day bearer token."))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("opaque")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }
}
