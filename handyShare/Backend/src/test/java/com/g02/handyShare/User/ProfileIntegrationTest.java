package com.g02.handyShare.User;

import com.g02.handyShare.CommonTestMethods;
import com.g02.handyShare.TestConstants;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProfileIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private String baseUrl;
    private String token;
    private User testUser;

    @BeforeEach
    void setUp() {
        baseUrl = String.format(TestConstants.BASE_URL, port);
        Map<String, Object> result = CommonTestMethods.registerAndLogin(restTemplate, userRepository, baseUrl);
        token = (String) result.get("token");
        testUser = (User) result.get("user");
        System.out.println("Token fetched is: " + token);
    }

    @Test
    void testProfileUpdateFormPrePopulated() {
        // Set headers with token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Fetch user details
        ResponseEntity<User> response = restTemplate.exchange(baseUrl + "/user/getUser", HttpMethod.GET, request, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getEmail(), user.getEmail());
    }

    @Test
    void testSuccessfulProfileUpdate() {
        // Set headers with token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create the request body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", "Updated User");
        body.add("address", "123 Updated Street");
        body.add("pincode", "12345");
        body.add("phone", "9876543210");
        body.add("profileImage", createMockProfileImage());

        // Wrap the request in HttpEntity
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        // Send PUT request
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/user/update-profile", HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Profile updated successfully"));

        // Verify update in the database
        User updatedUser = userRepository.findByEmail(testUser.getEmail());
        assertEquals("Updated User", updatedUser.getName());
        assertEquals("123 Updated Street", updatedUser.getAddress());
    }

    private ByteArrayResource createMockProfileImage() {
        byte[] fileContent = "Mock Image Content".getBytes(); // Mock content for testing
        return new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return "profileImage.jpg";
            }
        };
    }

    @AfterEach
    public void tearDown() {
        userRepository.delete(testUser);
    }
}