package com.g02.handyShare.User.Service;

import com.g02.handyShare.Constants;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService; // Injecting EmailService

    @Autowired
    private Constants constants;

    public String registerUser(User user) {
        // Check if email already exists
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {          
           return "already registered"; 
        }

        // Encrypt password before saving for new user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Generate a verification token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token); // Set the token in user entity

        // Create verification link
        String verificationLink = constants.SERVER_URL+"/api/v1/all/verifyUser?token=" + token;

        // Send the email to the user to verify
        String response = emailService.sendEmail(user.getEmail(), "Verify your email", verificationLink);

        if (response.contains("Success")) {
            // Save new user to the database
            userRepository.save(user);
            return "User registered successfully. Please check your email for verification.";
        }

        userRepository.save(user);
        return "Enter a valid email address.";
    }

    public User findByToken(String token) {
        return userRepository.findByVerificationToken(token); 
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String verifyUser(String token) {
       User existingUser = findByToken(token);
       if(existingUser != null){
        existingUser.set_email_verified(true);
        existingUser.setVerificationToken(null); 
        userRepository.save(existingUser);
        return "Successfully verified email";
       }
      return "Failed verifying the email";
    }

    public Optional<User> findUserById(Long UserId){
   
          
           Optional<User> owner = userRepository.findById(UserId);
return  owner;
    }
}
