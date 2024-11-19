package com.g02.handyShare.Config.Jwt;

import com.g02.handyShare.Config.CustomUserDetailsService;
import com.g02.handyShare.User.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtControllerTest {

    @InjectMocks
    private JwtController jwtController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
    }

   @Test
public void testGenToken_Success() {
    // Arrange
    UserDetails mockUserDetails = mock(UserDetails.class);
    
    // Ensure mocking is lenient for generateToken so it doesn't fail due to argument mismatch
    lenient().when(customUserDetailsService.loadUserByUsername(testUser.getEmail())).thenReturn(mockUserDetails);
    
    // Mock the generateToken method with the correct argument
    when(jwtUtil.generateToken(testUser.getEmail())).thenReturn("mock-jwt-token");

    // Act
    String jwtToken = jwtController.genToken(testUser);

    // Assert
    assertNotNull(jwtToken);
    assertEquals("mock-jwt-token", jwtToken);

    // Verify interactions
    verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(customUserDetailsService, times(1)).loadUserByUsername(testUser.getEmail());
    verify(jwtUtil, times(1)).generateToken(testUser.getEmail());
}


    @Test
    public void testGenToken_FailedAuthentication() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> jwtController.genToken(testUser));
        assertEquals("Authentication failed", exception.getMessage());
    }
}
