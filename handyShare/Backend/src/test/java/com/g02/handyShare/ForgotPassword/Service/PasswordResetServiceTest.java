package com.g02.handyShare.ForgotPassword.Service;

import com.g02.handyShare.Constants;
import com.g02.handyShare.User.DTO.ForgotPasswordRequest;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.User.Service.EmailService;
import com.g02.handyShare.User.Service.PasswordResetService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @InjectMocks
    private PasswordResetService passwordResetService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private Constants constants;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");
    }

    @Test
    void testForgotPassword_UserExists() {
        // Arrange
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@example.com");

        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        lenient().when(constants.getFrontEndHost()).thenReturn("http://mocked-host:3000");
        lenient().when(emailService.sendEmailToResetPassword(anyString(), anyString(), anyString()))
            .thenReturn("Successfully sent email to test@example.com");

        // Act
        String result = passwordResetService.forgotPassword(request);

        // Assert
        assertEquals("Password reset link sent to test@example.com", result);
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository).save(any(User.class));
        verify(emailService).sendEmailToResetPassword(anyString(), anyString(), anyString());
    }

    @Test
    void testForgotPassword_UserDoesNotExist() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("nonexistent@example.com");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        String result = passwordResetService.forgotPassword(request);

        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendEmailToResetPassword(any(), any(), any());
        assertEquals("User not found", result);
    }

    @Test
    void testResetPassword_ValidToken() {
        String token = UUID.randomUUID().toString();
        String newPassword = "newSecurePassword";

        user.setResetToken(token);
        user.setResetTokenExpiry(new Date(System.currentTimeMillis() + 3600000)); // 1 hour from now

        when(userRepository.findByResetToken(token)).thenReturn(user);

        String result = passwordResetService.resetPassword(token, newPassword);

        verify(userRepository, times(1)).save(user);
        assertEquals("Password reset successful", result);
    }

    @Test
    void testResetPassword_InvalidToken() {
        String token = "invalidToken";
        String newPassword = "newSecurePassword";

        when(userRepository.findByResetToken(token)).thenReturn(null);

        String result = passwordResetService.resetPassword(token, newPassword);

        verify(userRepository, never()).save(any());
        assertEquals("Invalid or expired token", result);
    }

    @Test
    void testResetPassword_ExpiredToken() {
        String token = UUID.randomUUID().toString();
        String newPassword = "newSecurePassword";

        user.setResetToken(token);
        user.setResetTokenExpiry(new Date(System.currentTimeMillis() - 3600000)); // 1 hour ago

        when(userRepository.findByResetToken(token)).thenReturn(user);

        String result = passwordResetService.resetPassword(token, newPassword);

        verify(userRepository, never()).save(any());
        assertEquals("Invalid or expired token", result);
    }
}

