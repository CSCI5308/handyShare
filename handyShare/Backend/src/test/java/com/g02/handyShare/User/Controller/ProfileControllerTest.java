package com.g02.handyShare.User.Controller;

import com.g02.handyShare.User.DTO.PasswordChangeRequest;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    @InjectMocks
    private Profile profileController;

    @Mock
    private ProfileService profileService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    // Test for fetchUser
    @Test
    void testFetchUser_Positive() {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        user.setName("Test User");

        when(authentication.getName()).thenReturn(email);
        when(profileService.getUser(email)).thenReturn(user);

        // Act
        ResponseEntity<?> response = profileController.fetchUser();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(profileService, times(1)).getUser(email);
    }

    @Test
    void testFetchUser_Negative_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(authentication.getName()).thenReturn(email);
        when(profileService.getUser(email)).thenReturn(null);

        // Act
        ResponseEntity<?> response = profileController.fetchUser();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(profileService, times(1)).getUser(email);
    }

    // Test for updateUser
    @Test
    void testUpdateUser_Positive() {
        // Arrange
        String email = "user@example.com";
        MockMultipartFile file = new MockMultipartFile("profileImage", "test.jpg", "image/jpeg", "image data".getBytes());
        User user = new User();
        user.setName("Updated User");
        user.setAddress("New Address");
        user.setPincode("123456");
        user.setPhone("9876543210");

        when(authentication.getName()).thenReturn(email);
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Profile updated successfully!");

        doReturn(mockResponse)
                .when(profileService)
                .modifyUser(
                        any(MultipartFile.class), any(User.class), anyString(), anyString()
                );


        // Act
        ResponseEntity<?> response = profileController.updateUser(file, "Updated User", "New Address", "123456", "9876543210");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("<200 OK OK,Profile updated successfully!,[]>", String.valueOf(response.getBody()));
        verify(profileService, times(1)).modifyUser(eq(file), any(User.class), eq(email), eq("user_profile_pictures"));
    }

    @Test
    void testUpdateUser_Negative_FileUploadFailed() {
        // Arrange
        String email = "user@example.com";
        MockMultipartFile file = new MockMultipartFile("profileImage", "test.jpg", "image/jpeg", "image data".getBytes());
        when(authentication.getName()).thenReturn(email);
        doAnswer(invocation -> ResponseEntity.status(500).body("File upload failed: Mocked error"))
                .when(profileService)
                .modifyUser(any(MultipartFile.class), any(User.class), anyString(), anyString());

        // Act
        ResponseEntity<?> response = profileController.updateUser(file, "Updated User", "New Address", "123456", "9876543210");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("<500 INTERNAL_SERVER_ERROR Internal Server Error,File upload failed: Mocked error,[]>", String.valueOf(response.getBody()));
    }

    // Test for changePassword
    @Test
    void testChangePassword_Positive() {
        // Arrange
        String email = "user@example.com";
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");

        when(authentication.getName()).thenReturn(email);
        when(profileService.changePassword(request, email)).thenReturn("Password changed successfully");

        // Act
        ResponseEntity<?> response = profileController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", response.getBody());
        verify(profileService, times(1)).changePassword(request, email);
    }

    @Test
    void testChangePassword_Negative_Unauthorized() {
        // Arrange
        String email = "user@example.com";
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword");

        when(authentication.getName()).thenReturn(email);
        when(profileService.changePassword(request, email)).thenReturn("Unauthorized operations");

        // Act
        ResponseEntity<?> response = profileController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Unauthorized operations", response.getBody());
    }
}