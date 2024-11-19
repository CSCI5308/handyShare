////package com.g02.handyShare.User.Service;
////
////import com.g02.handyShare.User.DTO.ForgotPasswordRequest;
////import com.g02.handyShare.User.Entity.User;
////import com.g02.handyShare.User.Repository.UserRepository;
////import org.junit.jupiter.api.BeforeEach;
////import org.junit.jupiter.api.Test;
////import org.mockito.*;
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////
////import java.util.Date;
////import java.util.UUID;
////
////import static org.junit.jupiter.api.Assertions.*;
////import static org.mockito.Mockito.*;
////
////public class PasswordResetServiceTest {
////
////    @Mock
////    private UserRepository userRepository;
////
////    @Mock
////    private EmailService emailService;
////
////    @Mock
////    private Constants constants;
////
////    @InjectMocks
////    private PasswordResetService passwordResetService;
////
////    private User user;
////    private ForgotPasswordRequest forgotPasswordRequest;
////
////    @BeforeEach
////    public void setUp() {
////        MockitoAnnotations.openMocks(this);
////
////        // Setup test user
////        user = new User();
////        user.setEmail("testuser@example.com");
////        user.setPassword("oldpassword");
////
////        // Setup ForgotPasswordRequest
////        forgotPasswordRequest = new ForgotPasswordRequest();
////        forgotPasswordRequest.setEmail("testuser@example.com");
////
////        // Setup constants mock
////        when(constants.FRONT_END_HOST).thenReturn("http://localhost:8080");
////    }
////
////    @Test
////    public void testForgotPassword_userNotFound() {
////        // Arrange
////        when(userRepository.findByEmail(forgotPasswordRequest.getEmail())).thenReturn(null);
////
////        // Act
////        String response = passwordResetService.forgotPassword(forgotPasswordRequest);
////
////        // Assert
////        assertEquals("User not found", response);
////    }
////
////    @Test
////    public void testForgotPassword_success() {
////        // Arrange
////        when(userRepository.findByEmail(forgotPasswordRequest.getEmail())).thenReturn(user);
////
////        // Act
////        String response = passwordResetService.forgotPassword(forgotPasswordRequest);
////
////        // Assert
////        assertEquals("Password reset link sent to testuser@example.com", response);
////        assertNotNull(user.getResetToken());
////        assertNotNull(user.getResetTokenExpiry());
////        verify(emailService, times(1)).sendEmailToResetPassword(anyString(), anyString(), anyString());
////    }
////
////    @Test
////    public void testResetPassword_invalidToken() {
////        // Arrange
////        String invalidToken = UUID.randomUUID().toString();
////        when(userRepository.findByResetToken(invalidToken)).thenReturn(null);
////
////        // Act
////        String response = passwordResetService.resetPassword(invalidToken, "newPassword");
////
////        // Assert
////        assertEquals("Invalid or expired token", response);
////    }
////
////    @Test
////    public void testResetPassword_expiredToken() {
////        // Arrange
////        String token = UUID.randomUUID().toString();
////        user.setResetToken(token);
////        user.setResetTokenExpiry(new Date(System.currentTimeMillis() - 3600000)); // expired 1 hour ago
////        when(userRepository.findByResetToken(token)).thenReturn(user);
////
////        // Act
////        String response = passwordResetService.resetPassword(token, "newPassword");
////
////        // Assert
////        assertEquals("Invalid or expired token", response);
////    }
////
////    @Test
////    public void testResetPassword_success() {
////        // Arrange
////        String token = UUID.randomUUID().toString();
////        user.setResetToken(token);
////        user.setResetTokenExpiry(new Date(System.currentTimeMillis() + 3600000)); // valid for 1 hour
////        when(userRepository.findByResetToken(token)).thenReturn(user);
////
////        // Act
////        String response = passwordResetService.resetPassword(token, "newPassword");
////
////        // Assert
////        assertEquals("Password reset successful", response);
////        assertNull(user.getResetToken());
////        assertNull(user.getResetTokenExpiry());
////        assertTrue(new BCryptPasswordEncoder().matches("newPassword", user.getPassword()));
////        verify(userRepository, times(1)).save(user);
////    }
////}
//
//package com.g02.handyShare.User.Service;
//
//import com.g02.handyShare.User.DTO.ForgotPasswordRequest;
//import com.g02.handyShare.User.Entity.User;
//import com.g02.handyShare.User.Repository.UserRepository;
//import com.g02.handyShare.Constants;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.util.Date;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class PasswordResetServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private EmailService emailService;
//
//    @Mock
//    private Constants constants;
//
//    @InjectMocks
//    private PasswordResetService passwordResetService;
//
//    private User user;
//    private ForgotPasswordRequest forgotPasswordRequest;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Setup test user
//        user = new User();
//        user.setEmail("testuser@example.com");
//        user.setPassword("oldpassword");
//
//        // Setup ForgotPasswordRequest
//        forgotPasswordRequest = new ForgotPasswordRequest();
//        forgotPasswordRequest.setEmail("testuser@example.com");
//
//        // Setup constants mock
//        when(constants.FRONT_END_HOST).thenReturn("http://localhost:8080");
//    }
//
//    @Test
//    public void testForgotPassword_userNotFound() {
//        // Arrange
//        when(userRepository.findByEmail(forgotPasswordRequest.getEmail())).thenReturn(null);
//
//        // Act
//        String response = passwordResetService.forgotPassword(forgotPasswordRequest);
//
//        // Assert
//        assertEquals("User not found", response);
//    }
//
//    @Test
//    public void testForgotPassword_success() {
//        // Arrange
//        when(userRepository.findByEmail(forgotPasswordRequest.getEmail())).thenReturn(user);
//
//        // Act
//        String response = passwordResetService.forgotPassword(forgotPasswordRequest);
//
//        // Assert
//        assertEquals("Password reset link sent to testuser@example.com", response);
//        assertNotNull(user.getResetToken());
//        assertNotNull(user.getResetTokenExpiry());
//        verify(emailService, times(1)).sendEmailToResetPassword(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    public void testResetPassword_invalidToken() {
//        // Arrange
//        String invalidToken = UUID.randomUUID().toString();
//        when(userRepository.findByResetToken(invalidToken)).thenReturn(null);
//
//        // Act
//        String response = passwordResetService.resetPassword(invalidToken, "newPassword");
//
//        // Assert
//        assertEquals("Invalid or expired token", response);
//    }
//
//    @Test
//    public void testResetPassword_expiredToken() {
//        // Arrange
//        String token = UUID.randomUUID().toString();
//        user.setResetToken(token);
//        user.setResetTokenExpiry(new Date(System.currentTimeMillis() - 3600000)); // expired 1 hour ago
//        when(userRepository.findByResetToken(token)).thenReturn(user);
//
//        // Act
//        String response = passwordResetService.resetPassword(token, "newPassword");
//
//        // Assert
//        assertEquals("Invalid or expired token", response);
//    }
//
//    @Test
//    public void testResetPassword_success() {
//        // Arrange
//        String token = UUID.randomUUID().toString();
//        user.setResetToken(token);
//        user.setResetTokenExpiry(new Date(System.currentTimeMillis() + 3600000)); // valid for 1 hour
//        when(userRepository.findByResetToken(token)).thenReturn(user);
//
//        // Act
//        String response = passwordResetService.resetPassword(token, "newPassword");
//
//        // Assert
//        assertEquals("Password reset successful", response);
//        assertNull(user.getResetToken());
//        assertNull(user.getResetTokenExpiry());
//        assertTrue(new BCryptPasswordEncoder().matches("newPassword", user.getPassword()));
//        verify(userRepository, times(1)).save(user);
//    }
//}
