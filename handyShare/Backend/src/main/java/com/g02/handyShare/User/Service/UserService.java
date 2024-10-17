package com.g02.handyShare.User.Service;

import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService; // Injecting EmailService

    public String registerUser(User user) {
        // Check if email already exists
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            // Update existing user if needed
          
            userRepository.save(existingUser); // Save updated user
            return "User updated successfully. Please check your email for verification."; // Return a success message
        }

        // Encrypt password before saving for new user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Generate a verification token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token); // Set the token in user entity

        // Create verification link
        String verificationLink = "http://localhost:8080/api/v1/all/verifyUser?token=" + token;

        // Send the email to the user to verify
        String response = emailService.sendEmail(user.getEmail(), "Verify your email",
                "Please verify your email by clicking on the following link: " + verificationLink);

        if (response.contains("Success")) {
            // Save new user to the database
            userRepository.save(user);
            return "User registered successfully. Please check your email for verification.";
        }
        return "Enter a valid email address.";
    }

    public User findByToken(String token) {
        return userRepository.findByVerificationToken(token); // Implement this method in your UserRepository
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
