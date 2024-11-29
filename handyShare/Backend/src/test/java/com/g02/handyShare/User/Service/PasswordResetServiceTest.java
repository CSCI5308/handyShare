package com.g02.handyShare.User.Service;

import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private TestConstants constants;

    @InjectMocks
    private PasswordResetService passwordResetService;

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

        when(userRepository.findByResetToken(token)).thenReturn(user);

        when(userRepository.save(user)).thenReturn(user);

        String result = passwordResetService.resetPassword(token, newPassword);

        assertEquals("Password reset successful", result);

        verify(userRepository, times(1)).findByResetToken(token);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testResetPassword_InvalidToken() {
        String token = "invalidToken";
        String newPassword = "newPassword123";

        when(userRepository.findByResetToken(token)).thenReturn(null);

        String result = passwordResetService.resetPassword(token, newPassword);

        assertEquals("Invalid or expired token", result);

        verify(userRepository, times(1)).findByResetToken(token);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testResetPassword_TokenExpired() {
        String token = "expiredToken";
        String newPassword = "newPassword123";

        User user = new User();
        user.setResetToken(token);
        user.setResetTokenExpiry(new Date(System.currentTimeMillis() - 3600000));

        when(userRepository.findByResetToken(token)).thenReturn(user);

        String result = passwordResetService.resetPassword(token, newPassword);

        assertEquals("Invalid or expired token", result);

        verify(userRepository, times(1)).findByResetToken(token);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testResetPassword_UserNotFound() {
        String token = "nonExistingToken";
        String newPassword = "newPassword123";

        when(userRepository.findByResetToken(token)).thenReturn(null);

        String result = passwordResetService.resetPassword(token, newPassword);

        assertEquals("Invalid or expired token", result);

        verify(userRepository, times(1)).findByResetToken(token);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testResetPassword_EmptyNewPassword() {
        String token = "validToken";
        String newPassword = ""; // Empty new password

        User user = new User();
        user.setResetToken(token);
        user.setResetTokenExpiry(new Date(System.currentTimeMillis() + 3600000));
        when(userRepository.findByResetToken(token)).thenReturn(user);

        String result = passwordResetService.resetPassword(token, newPassword);

        assertEquals("Invalid or expired token", result);

        verify(userRepository, times(0)).findByResetToken(token);
        verify(userRepository, never()).save(any());
    }
}
