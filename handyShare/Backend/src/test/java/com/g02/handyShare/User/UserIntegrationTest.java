package com.g02.handyShare.User;

import com.g02.handyShare.TestConstants;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.User.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = String.format(TestConstants.BASE_URL, port);
    }

    @Test
    void testRegistrationAndLoginFlow() {
        // Step 1: Register User
        User registerRequest = new User();
        registerRequest.setName("Yash");
        registerRequest.setEmail("yashharjani@yopmail.com");
        registerRequest.setPassword("12345678");

        ResponseEntity<String> registerResponse = restTemplate.postForEntity(baseUrl + "/all/register", registerRequest, String.class);

        System.out.println("Registration Response Status: " + registerResponse.getStatusCode());
        System.out.println("Registration Response Body: " + registerResponse.getBody());

        assertEquals(HttpStatus.OK, registerResponse.getStatusCode());
        assertTrue(registerResponse.getBody().contains("User registered successfully"));

        // Verify user is saved in the database
        User savedUser = userRepository.findByEmail("yashharjani@yopmail.com");
        assertNotNull(savedUser);

        // Simulate email verification
        userService.verifyUser(savedUser.getVerificationToken());

        // Step 2: Login User
        User loginRequest = new User();
        loginRequest.setEmail("yashharjani@yopmail.com");
        loginRequest.setPassword("12345678");

        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(baseUrl + "/all/login", loginRequest, Map.class);

        // Log the response for debugging
        System.out.println("Login Response Status: " + loginResponse.getStatusCode());
        System.out.println("Login Response Body: " + loginResponse.getBody());

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        Map<String, String> responseBody = loginResponse.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.get("token"));
        assertEquals("user", responseBody.get("role"));
        deleteUser(savedUser);
    }

    @Test
    void testForAlreadyRegisteredUser(){
        User registerRequest = new User();
        registerRequest.setName("Yash");
        registerRequest.setEmail("yashharjani9@yopmail.com");
        registerRequest.setPassword("12345678");

        ResponseEntity<String> registerResponse = restTemplate.postForEntity(baseUrl + "/all/register", registerRequest, String.class);

        System.out.println("Registration Response Status: " + registerResponse.getStatusCode());
        System.out.println("Registration Response Body: " + registerResponse.getBody());

        User registerRequest2 = new User();
        registerRequest2.setName("Yash");
        registerRequest2.setEmail("yashharjani9@yopmail.com");
        registerRequest2.setPassword("12345678");

        ResponseEntity<String> registerResponse2 = restTemplate.postForEntity(baseUrl + "/all/register", registerRequest, String.class);

        System.out.println("Response for second request: " +registerResponse2.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, registerResponse2.getStatusCode());
        assertTrue(registerResponse2.getBody().contains("already registered"));
        deleteUser(registerRequest);
    }

    @Test
    void testSessionPersistence() {
        User registerRequest = new User();
        registerRequest.setName("Test User");
        registerRequest.setEmail("testuser@yopmail.com");
        registerRequest.setPassword("testpassword");

        restTemplate.postForEntity(baseUrl + "/all/register", registerRequest, String.class);

        User savedUser = userRepository.findByEmail("testuser@yopmail.com");
        userService.verifyUser(savedUser.getVerificationToken());

        User loginRequest = new User();
        loginRequest.setEmail("testuser@yopmail.com");
        loginRequest.setPassword("testpassword");

        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(baseUrl + "/all/login", loginRequest, Map.class);

        String token = (String) loginResponse.getBody().get("token");
        assertNotNull(token);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String securedEndpoint = baseUrl + "/user/getTrendingByCategory?category=Electronics";

        ResponseEntity<Map> securedResponse = restTemplate.exchange(
                securedEndpoint,
                HttpMethod.GET,
                entity,
                Map.class
        );

        assertEquals(HttpStatus.OK, securedResponse.getStatusCode());

        deleteUser(savedUser);
    }

    private void deleteUser(User user){
        userRepository.delete(user);
    }
}