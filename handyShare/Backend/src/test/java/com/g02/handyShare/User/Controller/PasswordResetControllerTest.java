package com.g02.handyShare.User.Controller;

import com.g02.handyShare.Constants;
import com.g02.handyShare.User.Service.PasswordResetService;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordResetControllerTest {

    @Mock
    private PasswordResetService passwordResetService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Constants constants;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testResetPassword_Success() {
        String token = "validToken";
        String newPassword = "newPassword123";

        User user = new User();
        user.setResetToken(token);
        user.setResetTokenExpiry(new Date(System.currentTimeMillis() + 3600000)); // 1 hour expiry

        when(passwordResetService.resetPassword(token, newPassword)).thenReturn("Password reset successful");

        String response = String.valueOf(passwordResetController.resetPassword(token, Map.of("newPassword", newPassword)));

        assertEquals("<200 OK OK,Password reset successful,[]>", response);

        verify(passwordResetService, times(1)).resetPassword(token, newPassword);
    }

    @Test
    public void testResetPassword_InvalidToken() {
        String token = "invalidToken";
        String newPassword = "newPassword123";

        when(passwordResetService.resetPassword(token, newPassword)).thenReturn("Invalid or expired token");
        String response = String.valueOf(passwordResetController.resetPassword(token, Map.of("newPassword", newPassword)));
        assertEquals("<400 BAD_REQUEST Bad Request,Invalid or expired token,[]>", response);
        verify(passwordResetService, times(1)).resetPassword(token, newPassword);
    }

    @Test
    public void testResetPassword_TokenExpired() {
        String token = "expiredToken";
        String newPassword = "newPassword123";
        User user = new User();
        user.setResetToken(token);
        user.setResetTokenExpiry(new Date(System.currentTimeMillis() - 3600000)); // Expired token
        when(passwordResetService.resetPassword(token, newPassword)).thenReturn("Invalid or expired token");
        String response = String.valueOf(passwordResetController.resetPassword(token, Map.of("newPassword", newPassword)));
        assertEquals("<400 BAD_REQUEST Bad Request,Invalid or expired token,[]>", response);
        verify(passwordResetService, times(1)).resetPassword(token, newPassword);
    }

    @Test
        public void testResetPassword_MissingNewPassword() {
        String token = "validToken";
        String response = String.valueOf(passwordResetController.resetPassword(token, Map.of()));
        assertEquals("<400 BAD_REQUEST Bad Request,Required parameter 'newPassword' is missing,[]>", response);
        verify(passwordResetService, never()).resetPassword(any(), any());
    }
}