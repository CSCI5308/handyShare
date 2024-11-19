//package com.g02.handyShare.User.Service;
//
//import com.g02.handyShare.User.Entity.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class EmailServiceTest {
//
//    @InjectMocks
//    private EmailService emailService;
//
//    @Mock
//    private JavaMailSender mailSender;
//
//    @Mock
//    private MimeMessage mimeMessage;
//
//    @Mock
//    private MimeMessageHelper mimeMessageHelper;
//
//    @Mock
//    private User user;  // Mocking the User entity
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testSendEmailSuccessfully() throws MessagingException {
//        // Arrange
//        String to = "test@example.com";
//        String subject = "Email Verification";
//        String verificationLink = "https://example.com/verify?token=abc123";
//
//        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//        Mockito.when(mimeMessageHelper.setTo(to)).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setSubject(subject)).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setText(Mockito.anyString(), Mockito.eq(true))).thenReturn(mimeMessageHelper);
//        Mockito.doNothing().when(mailSender).send(mimeMessage);
//
//        // Act
//        String result = emailService.sendEmail(to, subject, verificationLink);
//
//        // Assert
//        assertEquals("Successfully sent email to test@example.com", result);
//    }
//
//    @Test
//    void testSendEmailFailure() throws MessagingException {
//        // Arrange
//        String to = "test@example.com";
//        String subject = "Email Verification";
//        String verificationLink = "https://example.com/verify?token=abc123";
//
//        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//        Mockito.when(mimeMessageHelper.setTo(to)).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setSubject(subject)).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setText(Mockito.anyString(), Mockito.eq(true))).thenReturn(mimeMessageHelper);
//        Mockito.doThrow(new MessagingException("SMTP error")).when(mailSender).send(mimeMessage);
//
//        // Act
//        String result = emailService.sendEmail(to, subject, verificationLink);
//
//        // Assert
//        assertEquals("Error while sending email.", result);
//    }
//
//    @Test
//    void testSendEmailToResetPasswordSuccessfully() throws MessagingException {
//        // Arrange
//        String to = "test@example.com";
//        String subject = "Password Reset";
//        String resetPasswordLink = "https://example.com/reset?token=abc123";
//
//        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//        Mockito.when(mimeMessageHelper.setTo(to)).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setSubject(subject)).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setText(Mockito.anyString(), Mockito.eq(true))).thenReturn(mimeMessageHelper);
//        Mockito.doNothing().when(mailSender).send(mimeMessage);
//
//        // Act
//        String result = emailService.sendEmailToResetPassword(to, subject, resetPasswordLink);
//
//        // Assert
//        assertEquals("Successfully sent email to test@example.com", result);
//    }
//
//    @Test
//    void testSendEmailToResetPasswordFailure() throws MessagingException {
//        // Arrange
//        String to = "test@example.com";
//        String subject = "Password Reset";
//        String resetPasswordLink = "https://example.com/reset?token=abc123";
//
//        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//        Mockito.when(mimeMessageHelper.setTo(to)).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setSubject(subject)).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setText(Mockito.anyString(), Mockito.eq(true))).thenReturn(mimeMessageHelper);
//        Mockito.doThrow(new MessagingException("SMTP error")).when(mailSender).send(mimeMessage);
//
//        // Act
//        String result = emailService.sendEmailToResetPassword(to, subject, resetPasswordLink);
//
//        // Assert
//        assertEquals("Error while sending email.", result);
//    }
//
//    @Test
//    void testUserEmailVerificationEmail() throws MessagingException {
//        // Arrange
//        String verificationLink = "https://example.com/verify?token=abc123";
//        String email = "test@example.com";
//        String subject = "Email Verification";
//        User mockUser = new User();
//        mockUser.setEmail(email);
//
//        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//        Mockito.when(mimeMessageHelper.setTo(mockUser.getEmail())).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setSubject(subject)).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setText(Mockito.anyString(), Mockito.eq(true))).thenReturn(mimeMessageHelper);
//        Mockito.doNothing().when(mailSender).send(mimeMessage);
//
//        // Act
//        String result = emailService.sendEmail(mockUser.getEmail(), subject, verificationLink);
//
//        // Assert
//        assertEquals("Successfully sent email to test@example.com", result);
//    }
//
//    @Test
//    void testUserPasswordResetEmail() throws MessagingException {
//        // Arrange
//        String resetPasswordLink = "https://example.com/reset?token=abc123";
//        String email = "test@example.com";
//        String subject = "Password Reset Request";
//        User mockUser = new User();
//        mockUser.setEmail(email);
//
//        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//        Mockito.when(mimeMessageHelper.setTo(mockUser.getEmail())).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setSubject(subject)).thenReturn(mimeMessageHelper);
//        Mockito.when(mimeMessageHelper.setText(Mockito.anyString(), Mockito.eq(true))).thenReturn(mimeMessageHelper);
//        Mockito.doNothing().when(mailSender).send(mimeMessage);
//
//        // Act
//        String result = emailService.sendEmailToResetPassword(mockUser.getEmail(), subject, resetPasswordLink);
//
//        // Assert
//        assertEquals("Successfully sent email to test@example.com", result);
//    }
//}
