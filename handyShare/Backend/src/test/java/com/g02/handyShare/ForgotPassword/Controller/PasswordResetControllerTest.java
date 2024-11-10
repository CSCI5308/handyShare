package com.g02.handyShare.ForgotPassword.Controller;

import com.g02.handyShare.User.Controller.PasswordResetController;
import com.g02.handyShare.User.DTO.ForgotPasswordRequest;
import com.g02.handyShare.User.Service.PasswordResetService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PasswordResetControllerTest {

    @InjectMocks
    private PasswordResetController passwordResetController;

    @Mock
    private PasswordResetService passwordResetService;

    public PasswordResetControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testForgotPassword_Success() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@example.com");

        when(passwordResetService.forgotPassword(request)).thenReturn("Password reset link sent to test@example.com");

        String response = passwordResetController.forgotPassword(request);
        assertEquals("Password reset link sent to test@example.com", response);
    }

    @Test
    void testForgotPassword_UserNotFound() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("notfound@example.com");

        when(passwordResetService.forgotPassword(request)).thenReturn("User not found");

        String response = passwordResetController.forgotPassword(request);
        assertEquals("User not found", response);
    }

    @Test
    void testResetPassword_Success() {
        String token = "validToken";
        Map<String, String> request = Map.of("newPassword", "newSecurePassword");

        when(passwordResetService.resetPassword(token, "newSecurePassword")).thenReturn("Password reset successful");

        ResponseEntity<String> response = passwordResetController.resetPassword(token, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset successful", response.getBody());
    }

    @Test
    void testResetPassword_InvalidToken() {
        String token = "invalidToken";
        Map<String, String> request = Map.of("newPassword", "newSecurePassword");

        when(passwordResetService.resetPassword(token, "newSecurePassword")).thenReturn("Invalid or expired token");

        ResponseEntity<String> response = passwordResetController.resetPassword(token, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid or expired token", response.getBody());
    }
}

