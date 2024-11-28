package com.g02.handyShare.Config.Jwt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testFilterWithValidJwtToken() throws ServletException, IOException {
        // Arrange
        String jwtToken = "valid.jwt.token";
        String username = "testUser";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtUtil.extractUsername(jwtToken)).thenReturn(username);
        when(jwtUtil.validateToken(jwtToken)).thenReturn(true);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, times(1)).extractUsername(jwtToken);
        verify(jwtUtil, times(1)).validateToken(jwtToken);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
        verify(securityContext, times(1)).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testFilterWithNoAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(jwtUtil, never()).validateToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testFilterWithInvalidJwtToken() throws ServletException, IOException {
        // Arrange
        String jwtToken = "invalid.jwt.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtUtil.extractUsername(jwtToken)).thenReturn(null);
        when(jwtUtil.validateToken(jwtToken)).thenReturn(false);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testFilterWithInvalidAuthorizationHeaderFormat() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidHeaderFormat");

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(jwtUtil, never()).validateToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testFilterWithNullUsername() throws ServletException, IOException {
        // Arrange
        String jwtToken = "valid.jwt.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtUtil.extractUsername(jwtToken)).thenReturn(null);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, times(1)).extractUsername(jwtToken);
        verify(jwtUtil, never()).validateToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testFilterWhenJwtUtilThrowsException() throws ServletException, IOException {
        // Arrange
        String jwtToken = "invalid.jwt.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtUtil.extractUsername(jwtToken)).thenThrow(new RuntimeException("JWT parsing error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> jwtFilter.doFilterInternal(request, response, filterChain));

        // Verify interactions
        verify(filterChain, never()).doFilter(request, response); // Ensure the chain does not proceed
        verify(jwtUtil, times(1)).extractUsername(jwtToken);      // Ensure the method is called
        verify(jwtUtil, never()).validateToken(anyString());      // Ensure validation is not attempted
        verify(userDetailsService, never()).loadUserByUsername(anyString()); // Ensure no user details are loaded
        verify(securityContext, never()).setAuthentication(any());          // Ensure no authentication is set
    }

}