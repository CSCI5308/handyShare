package com.g02.handyShare.User.Controller;

import com.g02.handyShare.User.DTO.ForgotPasswordRequest;
import com.g02.handyShare.User.Service.PasswordResetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PasswordResetControllerTest {

    @Mock
    private PasswordResetService passwordResetService; // Mock the service

    @InjectMocks
    private PasswordResetController passwordResetController; // Inject mocks into the controller

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    void testForgotPassword_Success() {
        // Arrange
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@example.com");
        String expectedResponse = "Password reset link sent successfully";

        when(passwordResetService.forgotPassword(request)).thenReturn(expectedResponse);

        // Act
        String response = passwordResetController.forgotPassword(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(passwordResetService, times(1)).forgotPassword(request);
    }

    @Test
    void testForgotPassword_Failure() {
        // Arrange
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("invalid@example.com");
        String expectedResponse = "User not found";

        when(passwordResetService.forgotPassword(request)).thenReturn(expectedResponse);

        // Act
        String response = passwordResetController.forgotPassword(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(passwordResetService, times(1)).forgotPassword(request);
    }

    @Test
    void testResetPassword_Success() {
        // Arrange
        String token = "valid-token";
        String newPassword = "newPassword123";
        Map<String, String> request = Map.of("newPassword", newPassword);
        String expectedResponse = "Password reset successful";

        when(passwordResetService.resetPassword(token, newPassword)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> responseEntity = passwordResetController.resetPassword(token, request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(passwordResetService, times(1)).resetPassword(token, newPassword);
    }

    @Test
    void testResetPassword_Failure() {
        // Arrange
        String token = "invalid-token";
        String newPassword = "newPassword123";
        Map<String, String> request = Map.of("newPassword", newPassword);
        String expectedResponse = "Invalid token";

        when(passwordResetService.resetPassword(token, newPassword)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> responseEntity = passwordResetController.resetPassword(token, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(passwordResetService, times(1)).resetPassword(token, newPassword);
    }
}
