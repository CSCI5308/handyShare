package com.g02.handyShare;

import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommonTestMethods {

    public static Map<String, Object> registerAndLogin(TestRestTemplate restTemplate, UserRepository userRepository, String baseUrl) {
        // Register a new user
        User registerRequest = new User();
        registerRequest.setName("Test User");
        registerRequest.setEmail("testuser" + System.currentTimeMillis() + "@example.com");
        registerRequest.setPassword("password123");

        restTemplate.postForEntity(baseUrl + "/all/register", registerRequest, String.class);

        // Simulate email verification
        User testUser = userRepository.findByEmail(registerRequest.getEmail());
        testUser.set_email_verified(true);
        userRepository.save(testUser);

        // Login to get the JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        User loginRequest = new User();
        loginRequest.setEmail(registerRequest.getEmail());
        loginRequest.setPassword("password123");
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(baseUrl + "/all/login", loginRequest, Map.class);

        String token = (String) loginResponse.getBody().get("token");

        // Return both token and user
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", testUser);

        return result;
    }
}