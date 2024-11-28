package com.g02.handyShare.User.Controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.g02.handyShare.User.Service.UserService;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.Config.Jwt.JwtUtil;
import com.g02.handyShare.Config.CustomUserDetailsService;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.DTO.LenderDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    private User testUser;
    private LenderDetailsDTO lenderDetailsDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1L, "John Doe", "john@example.com", "password", "user", true, "token", "address", "12345", "555-5555", "imageData", null, null);
        lenderDetailsDTO = new LenderDetailsDTO(1L, "John Doe", "john@example.com", "address", "555-5555", "12345", "imageData", null);
    }

    @Test
    public void testRegisterUser_Success() {
        when(userService.registerUser(any(User.class))).thenReturn("User registered successfully. Please check your email for verification.");

        ResponseEntity<String> response = userController.registerUser(testUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully. Please check your email for verification.", response.getBody());
    }
    @Test
    public void testRegisterUser_EmailAlreadyRegistered() {
        when(userService.registerUser(any(User.class))).thenReturn("already registered");

        ResponseEntity<String> response = userController.registerUser(testUser);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("already registered", response.getBody());
    }

    @Test
    public void testRegisterUser_EmailIsNull() {
        testUser.setEmail(null);

        ResponseEntity<String> response = userController.registerUser(testUser);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email is required", response.getBody());
    }

    @Test
    public void testGetUsers_Admin() {
        when(userService.getAllUsers()).thenReturn(List.of(testUser));

        ResponseEntity<List<User>> response = userController.getUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testVerifyEmail_Success() {
        when(userService.verifyUser("valid-token")).thenReturn("Successfully verified email");

        ResponseEntity<String> response = userController.verifyEmail("valid-token");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Email verified", response.getBody());
    }

    @Test
    public void testVerifyEmail_InvalidToken() {
        when(userService.verifyUser("invalid-token")).thenReturn("Failed verifying the email");

        ResponseEntity<String> response = userController.verifyEmail("invalid-token");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid or expired token.", response.getBody());
    }

//    @Test
//    public void testLogin_Success() {
//        // Create a mock Authentication object
//        Authentication authentication = mock(Authentication.class);
//        when(authenticationManager.authenticate(any())).thenReturn(authentication);
//
//        // Mock UserDetails and return it when loadUserByUsername is called
//        UserDetails userDetails = mock(UserDetails.class);
//        when(customUserDetailsService.loadUserByUsername("yashharjani1@yopmail.com")).thenReturn(userDetails);
//        when(userDetails.getUsername()).thenReturn("yashharjani1@yopmail.com");
//
//        // Generate token mock
//        when(jwtUtil.generateToken("yashharjani1@yopmail.com")).thenReturn("jwt-token");
//
//        // Mock the repository to return a test user
//        when(userRepository.findByEmail("yashharjani1@yopmail.com")).thenReturn(testUser);
//
//        // Call the login method
//        ResponseEntity<Map<String, String>> response = userController.login(testUser);
//
//        // Assert the response
//        assertEquals(200, response.getStatusCodeValue());
//        assertTrue(response.getBody().containsKey("token"));
//        assertTrue(response.getBody().containsKey("role"));
//    }

    @Test
    public void testLogin_BadCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Bad credentials"));

        ResponseEntity<Map<String, String>> response = userController.login(testUser);

        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Bad credentials!", response.getBody().get("error"));
    }

    @Test
    public void testGetLenderDetails_Success() {
        when(userService.getLenderDetails(1L)).thenReturn(lenderDetailsDTO);

        ResponseEntity<?> response = userController.getLenderDetails(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof LenderDetailsDTO);
    }

    @Test
    public void testGetLenderDetails_NotFound() {
        when(userService.getLenderDetails(1L)).thenThrow(new RuntimeException("Lender not found"));

        ResponseEntity<?> response = userController.getLenderDetails(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Lender not found", response.getBody());
    }



}

