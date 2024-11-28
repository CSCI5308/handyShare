package com.g02.handyShare.Config;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

class CorsFilterTest {

    @InjectMocks
    private CorsFilter corsFilter;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testOptionsRequestSetsHeadersAndEndsProcessing() throws ServletException, IOException {
        // Arrange
        request.setMethod("OPTIONS");

        // Act
        corsFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals("http://172.17.0.99:3000", response.getHeader("Access-Control-Allow-Origin"));
        assertEquals("GET, POST, PUT, DELETE, OPTIONS, PATCH", response.getHeader("Access-Control-Allow-Methods"));
        assertEquals("3600", response.getHeader("Access-Control-Max-Age"));
        assertEquals("authorization, content-type, xsrf-token", response.getHeader("Access-Control-Allow-Headers"));
        assertEquals("xsrf-token", response.getHeader("Access-Control-Expose-Headers"));
        assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));
        assertEquals(MockHttpServletResponse.SC_OK, response.getStatus());

        verifyNoInteractions(filterChain);
    }

    @Test
    void testNonOptionsRequestSetsHeadersAndProcessesFurther() throws ServletException, IOException {
        // Arrange
        request.setMethod("GET");

        // Act
        corsFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals("http://172.17.0.99:3000", response.getHeader("Access-Control-Allow-Origin"));
        assertEquals("GET, POST, PUT, DELETE, OPTIONS, PATCH", response.getHeader("Access-Control-Allow-Methods"));
        assertEquals("3600", response.getHeader("Access-Control-Max-Age"));
        assertEquals("authorization, content-type, xsrf-token", response.getHeader("Access-Control-Allow-Headers"));
        assertEquals("xsrf-token", response.getHeader("Access-Control-Expose-Headers"));
        assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testPostRequestSetsHeadersAndProcessesFurther() throws ServletException, IOException {
        // Arrange
        request.setMethod("POST");

        // Act
        corsFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals("http://172.17.0.99:3000", response.getHeader("Access-Control-Allow-Origin"));
        assertEquals("GET, POST, PUT, DELETE, OPTIONS, PATCH", response.getHeader("Access-Control-Allow-Methods"));
        assertEquals("3600", response.getHeader("Access-Control-Max-Age"));
        assertEquals("authorization, content-type, xsrf-token", response.getHeader("Access-Control-Allow-Headers"));
        assertEquals("xsrf-token", response.getHeader("Access-Control-Expose-Headers"));
        assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testAllowCredentialsHeaderIsSetToTrue() throws ServletException, IOException {
        // Arrange
        request.setMethod("GET");

        // Act
        corsFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));
    }
}