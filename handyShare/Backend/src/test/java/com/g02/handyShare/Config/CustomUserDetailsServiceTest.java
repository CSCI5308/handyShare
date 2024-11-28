package com.g02.handyShare.Config;
import com.g02.handyShare.Config.CustomUserDetailsService;

import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        // Initialize a mock user for testing
        mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setRole("ROLE_USER");
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        // Arrange: Mock the UserRepository to return a user when the email is provided
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        // Act: Call the loadUserByUsername method
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        // Assert: Verify the returned user details
        assertNotNull(userDetails, "UserDetails should not be null");
        assertEquals("test@example.com", userDetails.getUsername(), "Username should match");
        assertEquals("password123", userDetails.getPassword(), "Password should match");
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")), "User role should match");
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange: Mock the UserRepository to return null when the email is provided
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        // Act and Assert: Call the loadUserByUsername method and expect a UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonexistent@example.com");
        }, "Expected UsernameNotFoundException when user not found");
    }

    @Test
    public void testLoadUserByUsername_EmptyEmail() {
        // Act and Assert: Call the loadUserByUsername method with an empty email and expect a UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("");
        }, "Expected UsernameNotFoundException when email is empty");
    }

    @Test
    public void testLoadUserByUsername_NullEmail() {
        // Act and Assert: Call the loadUserByUsername method with a null email and expect a UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(null);
        }, "Expected UsernameNotFoundException when email is null");
    }
}
