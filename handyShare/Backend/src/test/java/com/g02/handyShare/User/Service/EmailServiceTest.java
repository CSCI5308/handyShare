package com.g02.handyShare.User.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mimeMessage = mock(MimeMessage.class);
    }

    @Test
    void testSendEmail_Positive() {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        String to = "test@example.com";
        String subject = "Welcome to HandyShare!";
        String verificationLink = "http://example.com/verify?token=123";

        // Act
        String result = emailService.sendEmail(to, subject, verificationLink);

        // Assert
        assertEquals("Successfully sent email to " + to, result);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmail_Negative_MessagingException() throws MessagingException {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        MimeMessageHelper helper = mock(MimeMessageHelper.class);
        doThrow(new MessagingException("Mocked Messaging Exception"))
                .when(helper)
                .setText(anyString(), eq(true));

        String to = "test@example.com";
        String subject = "Welcome to HandyShare!";
        String verificationLink = "http://example.com/verify?token=123";

        // Act
        String result = emailService.sendEmail(to, subject, verificationLink);

        // Assert
        assertEquals("Successfully sent email to test@example.com", result);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmailToResetPassword_Positive() {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        String to = "test@example.com";
        String subject = "Reset Your Password";
        String resetPasswordLink = "http://example.com/reset?token=123";

        // Act
        String result = emailService.sendEmailToResetPassword(to, subject, resetPasswordLink);

        // Assert
        assertEquals("Successfully sent email to " + to, result);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmailToResetPassword_Negative_MessagingException() {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        String to = "test@example.com";
        String subject = "Reset Your Password";
        String resetPasswordLink = "http://example.com/reset?token=123";

        String result = emailService.sendEmailToResetPassword(to, subject, resetPasswordLink);

        // Assert
        assertEquals("Successfully sent email to test@example.com", result);
    }

    @Test
    void testSendEmail_InvalidRecipient() {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        String to = ""; // Invalid email
        String subject = "Welcome to HandyShare!";
        String verificationLink = "http://example.com/verify?token=123";

        // Act
        String result = emailService.sendEmail(to, subject, verificationLink);

        // Assert
        assertEquals("Error while sending email.", result); // Assuming the service handles invalid emails gracefully.
        verify(mailSender, never()).send(any(MimeMessage.class));
    }
}