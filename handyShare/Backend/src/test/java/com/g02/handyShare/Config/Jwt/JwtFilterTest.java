package com.g02.handyShare.Config.Jwt;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

public class JwtFilterTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private JwtFilter jwtFilter;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        jwtFilter = new JwtFilter();
    
        // Set the private fields via reflection
        Field userDetailsServiceField = JwtFilter.class.getDeclaredField("userDetailsService");
        userDetailsServiceField.setAccessible(true);
        userDetailsServiceField.set(jwtFilter, userDetailsService);
    
        Field jwtUtilField = JwtFilter.class.getDeclaredField("jwtUtil");
        jwtUtilField.setAccessible(true);
        jwtUtilField.set(jwtFilter, jwtUtil);
    }

    @Test
    public void testDoFilterInternal_ValidToken_SetsAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "valid-jwt-token";
        String username = "test@example.com";
        UserDetails userDetails = User.withUsername(username).password("password").roles("USER").build();
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert: Ensure that the authentication is set in SecurityContextHolder
        verify(jwtUtil, times(1)).extractUsername(token);
        verify(jwtUtil, times(1)).validateToken(token);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_InvalidToken_DoesNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "invalid-jwt-token";
        String username = "test@example.com";
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(jwtUtil.validateToken(token)).thenReturn(false);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert: Ensure that the authentication is not set in SecurityContextHolder
        verify(jwtUtil, times(1)).extractUsername(token);
        verify(jwtUtil, times(1)).validateToken(token);
        verify(userDetailsService, times(0)).loadUserByUsername(username);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_NoToken_DoesNotSetAuthentication() throws ServletException, IOException {
        // Arrange: No Authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert: Ensure that the authentication is not set in SecurityContextHolder
        verify(jwtUtil, times(0)).extractUsername(anyString());
        verify(jwtUtil, times(0)).validateToken(anyString());
        verify(userDetailsService, times(0)).loadUserByUsername(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_ValidToken_FiltersChainCalled() throws ServletException, IOException {
        // Arrange
        String token = "valid-jwt-token";
        String username = "test@example.com";
        UserDetails userDetails = User.withUsername(username).password("password").roles("USER").build();
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert: Ensure the filter chain is called after processing
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
