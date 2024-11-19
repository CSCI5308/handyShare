//package com.g02.handyShare.User.Controller;
//
//import com.g02.handyShare.User.Service.ProfileService;
//import com.g02.handyShare.User.Service.UserService;
//import com.g02.handyShare.User.Entity.User;
//import com.g02.handyShare.User.Repository.UserRepository;
//import com.g02.handyShare.Config.CustomUserDetailsService;
//import com.g02.handyShare.Config.Jwt.JwtUtil;
//import com.g02.handyShare.User.DTO.PasswordChangeRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.ResponseEntity;
//import org.mockito.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(Profile.class)
//public class ProfileControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProfileService profileService;
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
//        mockUser.setName("John Doe");
//        mockUser.setEmail("test@example.com");
//        mockUser.setAddress("123 Main St");
//        mockUser.setPincode("12345");
//        mockUser.setPhone("1234567890");
//    }
//
//    @Test
//    void testFetchUser() throws Exception {
//        when(profileService.getUser(anyString())).thenReturn(mockUser);
//
//        mockMvc.perform(get("/api/v1/user/getUser")
//                        .header("Authorization", "Bearer mockJwtToken"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("John Doe"))
//                .andExpect(jsonPath("$.email").value("test@example.com"));
//    }
//
//    @Test
//    void testUpdateUser() throws Exception {
//        // Mocking the file as MultipartFile
//        MockMultipartFile file = new MockMultipartFile("profileImage", "test.jpg", "image/jpeg", new byte[0]);
//
//        // Create a mock User object
//        User updatedUser = new User();
//        updatedUser.setName("John Updated");
//        updatedUser.setAddress("456 Updated St");
//        updatedUser.setPincode("54321");
//        updatedUser.setPhone("0987654321");
//
//        // Mocking the ResponseEntity return type for modifyUser method
//        ResponseEntity<String> responseEntity = ResponseEntity.ok("Profile updated successfully!");
//
//        // Mocking the behavior of profileService.modifyUser
//        when(profileService.modifyUser(any(), any(), anyString(), anyString())).thenReturn(responseEntity);
//
//        // Performing the multipart request
//        mockMvc.perform(multipart("/api/v1/user/update-profile")
//                        .file(file)
//                        .param("name", "John Updated")
//                        .param("address", "456 Updated St")
//                        .param("pincode", "54321")
//                        .param("phone", "0987654321")
//                        .header("Authorization", "Bearer mockJwtToken"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").value("Profile updated successfully!"));
//    }
//
//
//    @Test
//    void testChangePassword() throws Exception {
//        PasswordChangeRequest request = new PasswordChangeRequest("oldPassword", "newPassword");
//
//        when(profileService.changePassword(any(), anyString())).thenReturn("Password changed successfully");
//
//        mockMvc.perform(post("/api/v1/user/changePassword")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"oldPassword\": \"oldPassword\", \"newPassword\": \"newPassword\"}")
//                        .header("Authorization", "Bearer mockJwtToken"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Password changed successfully"));
//    }
//
//    @Test
//    void testFetchUserNotFound() throws Exception {
//        when(profileService.getUser(anyString())).thenReturn(null);
//
//        mockMvc.perform(get("/api/v1/user/getUser")
//                        .header("Authorization", "Bearer mockJwtToken"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("User not found"));
//    }
//
//    @Test
//    void testUpdateUserFailure() throws Exception {
//        MockMultipartFile file = new MockMultipartFile("profileImage", "test.jpg", "image/jpeg", new byte[0]);
//
//        when(profileService.modifyUser(any(), any(), anyString(), anyString()))
//                .thenThrow(new RuntimeException("Update failed"));
//
//        mockMvc.perform(multipart("/api/v1/user/update-profile")
//                        .file(file)
//                        .param("name", "John Updated")
//                        .param("address", "456 Updated St")
//                        .param("pincode", "54321")
//                        .param("phone", "0987654321")
//                        .header("Authorization", "Bearer mockJwtToken"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string("Update failed"));
//    }
//
//    @Test
//    void testChangePasswordFailure() throws Exception {
//        PasswordChangeRequest request = new PasswordChangeRequest("wrongOldPassword", "newPassword");
//
//        when(profileService.changePassword(any(), anyString())).thenReturn("Password change failed");
//
//        mockMvc.perform(post("/api/v1/user/changePassword")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"oldPassword\": \"wrongOldPassword\", \"newPassword\": \"newPassword\"}")
//                        .header("Authorization", "Bearer mockJwtToken"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Password change failed"));
//    }
//}
