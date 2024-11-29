package com.g02.handyShare.User.Service;

import com.g02.handyShare.Constants;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Service.ProductService;
import com.g02.handyShare.User.DTO.LenderDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

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

    @Autowired
    private ProductService productService;

    @Autowired
    private Environment environment;

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

        if (isTestEnvironment()) {
            System.out.println("Test environment detected. Skipping email verification.");
            user.set_email_verified(true);
        } else {
            String verificationLink = constants.SERVER_URL+"/api/v1/all/verifyUser?token=" + token;
            String response = emailService.sendEmail(user.getEmail(), "Verify your email", verificationLink);
            System.out.println("Email sending response: " + response);
        }

        User savedUser = userRepository.save(user);
        System.out.println("User saved successfully: " + savedUser.getId());
        return "User registered successfully. " + (isTestEnvironment() ? "Email verification skipped in test environment." : "Please check your email for verification.");
    }

    private boolean isTestEnvironment() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("test".equals(profile)) {
                return true;
            }
        }
        return false;
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

    public LenderDetailsDTO getLenderDetails(Long lenderId) {
        User lender = userRepository.findById(lenderId)
                .orElseThrow(() -> new RuntimeException("Lender not found with id: " + lenderId));
        List<Product> products = productService.getProductsByLenderEmail(lender.getEmail());

        return new LenderDetailsDTO(
                lender.getId(),
                lender.getName(),
                lender.getEmail(),
                lender.getAddress(),
                lender.getPhone(),
                lender.getPincode(),
                lender.getImageData(),
                products
        );
    }
}
