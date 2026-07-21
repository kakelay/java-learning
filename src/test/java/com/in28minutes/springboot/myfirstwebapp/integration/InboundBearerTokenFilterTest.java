package com.in28minutes.springboot.myfirstwebapp.integration;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class InboundBearerTokenFilterTest {

    @Test
    void shouldSkipFilteringForUserApiEndpoints() {
        InboundBearerTokenFilter filter = new InboundBearerTokenFilter(
                mock(AuthService.class),
                mock(TokenManager.class));

        HttpServletRequest request = new MockHttpServletRequest("GET", "/api/user/v2/activeUser");

        assertTrue(filter.shouldNotFilter(request));
    }
}
