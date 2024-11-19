//package com.g02.handyShare.User.Controller;
//import com.g02.handyShare.User.DTO.LenderDetailsDTO;
//
//import com.g02.handyShare.User.Controller.UserController;
//import com.g02.handyShare.User.Service.UserService;
//import com.g02.handyShare.User.Entity.User;
//import com.g02.handyShare.User.Repository.UserRepository;
//import com.g02.handyShare.Config.CustomUserDetailsService;
//import com.g02.handyShare.Config.Jwt.JwtUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserController.class)
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private CustomUserDetailsService customUserDetailsService;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @MockBean
//    private JwtUtil jwtUtil;
//
//    @MockBean
//    private AuthenticationManager authenticationManager;
//
//    private User mockUser;
//
//    @BeforeEach
//    void setUp() {
//        mockUser = new User();
//        mockUser.setId(1L);
//        mockUser.setEmail("test@example.com");
//        mockUser.setPassword("password123");
//        mockUser.setRole("user");
//        mockUser.setEmailVerified(true);
//    }
//
//    @Test
//    void testRegisterUserSuccess() throws Exception {
//        when(userService.registerUser(any(User.class))).thenReturn("Registration successful");
//
//        mockMvc.perform(post("/api/v1/all/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\": \"John Doe\", \"email\": \"test@example.com\", \"password\": \"password123\"}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Registration successful"));
//    }
//
//    @Test
//    void testRegisterUserEmailRequired() throws Exception {
//        mockMvc.perform(post("/api/v1/all/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\": \"John Doe\", \"password\": \"password123\"}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Email is required"));
//    }
//
//    @Test
//    void testGetUsersAsAdmin() throws Exception {
//        when(userService.getAllUsers()).thenReturn(List.of(mockUser));
//
//        mockMvc.perform(get("/api/v1/admin/getUser"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].email").value("test@example.com"));
//    }
//
//    @Test
//    void testLoginSuccess() throws Exception {
//        when(userRepository.findByEmail(anyString())).thenReturn(mockUser);
//        when(jwtUtil.generateToken(anyString())).thenReturn("mockJwtToken");
//
//        mockMvc.perform(post("/api/v1/all/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("mockJwtToken"))
//                .andExpect(jsonPath("$.role").value("user"));
//    }
//
//    @Test
//    void testLoginEmailNotVerified() throws Exception {
//        mockUser.setEmailVerified(false);
//        when(userRepository.findByEmail(anyString())).thenReturn(mockUser);
//        when(jwtUtil.generateToken(anyString())).thenReturn("mockJwtToken");
//
//        mockMvc.perform(post("/api/v1/all/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error").value("Verify email first"));
//    }
//
//    @Test
//    void testLoginBadCredentials() throws Exception {
//        mockMvc.perform(post("/api/v1/all/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\": \"wrong@example.com\", \"password\": \"wrongpassword\"}"))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.error").value("Bad credentials!"));
//    }
//
//    @Test
//    void testVerifyEmailSuccess() throws Exception {
//        when(userService.verifyUser(anyString())).thenReturn("Successfully verified");
//
//        mockMvc.perform(get("/api/v1/all/verifyUser")
//                        .param("token", "validToken"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Email varified"));
//    }
//
//    @Test
//    void testVerifyEmailFailure() throws Exception {
//        when(userService.verifyUser(anyString())).thenReturn("Invalid token");
//
//        mockMvc.perform(get("/api/v1/all/verifyUser")
//                        .param("token", "invalidToken"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Invalid or expired token."));
//    }
//
//    @Test
//    void testGetLenderDetailsSuccess() throws Exception {
//        LenderDetailsDTO lenderDetailsDTO = new LenderDetailsDTO();
//        lenderDetailsDTO.setName("John Doe");
//        when(userService.getLenderDetails(anyLong())).thenReturn(lenderDetailsDTO);
//
//        mockMvc.perform(get("/api/v1/user/lender/{id}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("John Doe"));
//    }
//
//    @Test
//    void testGetLenderDetailsNotFound() throws Exception {
//        when(userService.getLenderDetails(anyLong())).thenThrow(new RuntimeException("Not found"));
//
//        mockMvc.perform(get("/api/v1/user/lender/{id}", 999L))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Not found"));
//    }
//}
