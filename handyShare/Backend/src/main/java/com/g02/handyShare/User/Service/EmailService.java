package com.g02.handyShare.User.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public String sendEmail(String to, String subject, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();

        String emailBody = "<html>"
                + "<body>"
                + "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f4f4f4;'>"
                + "<h2 style='color: #333;'>Welcome to HandyShare!</h2>"
                + "<p>Thank you for registering with us. Please verify your email address to activate your account by clicking the button below:</p>"
                + "<a href='" + verificationLink + "' style='display: inline-block; padding: 10px 20px; background-color: #28a745; color: #fff; text-decoration: none; font-weight: bold; border-radius: 5px;'>Verify Email</a>"
                + "<p>If the button above doesn't work, copy and paste the following link into your browser:</p>"
                + "<p style='word-break: break-all;'>" + verificationLink + "</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(emailBody, true); 
            mailSender.send(mimeMessage);
            return "Successfully sent email to " + to;
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while sending email.";
        }
    }

    public String sendEmailToResetPassword(String to, String subject, String resetPasswordLink) {
        SimpleMailMessage message = new SimpleMailMessage();

        String emailBody = "<html>"
                + "<body>"
                + "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f4f4f4;'>"
                + "<h2 style='color: #333;'>Reset Your Password!</h2>"
                + "<p>Reset you password by clicking the button below:</p>"
                + "<a href='" + resetPasswordLink + "' style='display: inline-block; padding: 10px 20px; background-color: #28a745; color: #fff; text-decoration: none; font-weight: bold; border-radius: 5px;'>Reset Password</a>"
                + "<p>If the button above doesn't work, copy and paste the following link into your browser:</p>"
                + "<p style='word-break: break-all;'>" + resetPasswordLink + "</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(emailBody, true); 
            mailSender.send(mimeMessage);
            return "Successfully sent email to " + to;
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while sending email.";
        }
    }
}