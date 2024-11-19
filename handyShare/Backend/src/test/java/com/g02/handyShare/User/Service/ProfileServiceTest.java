//package com.g02.handyShare.User.Service;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.g02.handyShare.Config.Firebase.FirebaseService;
//import com.g02.handyShare.User.DTO.PasswordChangeRequest;
//import com.g02.handyShare.User.Entity.User;
//import com.g02.handyShare.User.Repository.UserRepository;
//
//import java.io.IOException;
//
//public class ProfileServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private FirebaseService firebaseService;
//
//    @Mock
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private ProfileService profileService;
//
//    private User mockUser;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Set up a mock user for testing
//        mockUser = new User();
//        mockUser.setEmail("test@example.com");
//        mockUser.setName("John Doe");
//        mockUser.setPassword("$2a$10$7Xzss5F8x1ySse6/tEY1beNf11o/mbN5u5whZGv5tEopnxsqOyaSO"); // Encoded password
//    }
//
//    @Test
//    void testGetUser() {
//        // Arrange
//        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
//
//        // Act
//        User result = profileService.getUser("test@example.com");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("test@example.com", result.getEmail());
//    }
//
//    @Test
//    void testModifyUser_Success() throws IOException {
//        // Arrange
//        MultipartFile mockFile = mock(MultipartFile.class);
//        when(firebaseService.uploadFile(mockFile, "profile_pictures")).thenReturn("http://firebaseurl.com/profile.jpg");
//        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
//
//        // Act
//        ResponseEntity<?> response = profileService.modifyUser(mockFile, mockUser, "test@example.com", "profile_pictures");
//
//        // Assert
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Profile updated successfully!", response.getBody());
//        verify(userRepository).save(mockUser);
//    }
//
//    @Test
//    void testModifyUser_FileUploadFailure() throws IOException {
//        // Arrange
//        MultipartFile mockFile = mock(MultipartFile.class);
//        when(firebaseService.uploadFile(mockFile, "profile_pictures")).thenThrow(new IOException("Upload failed"));
//        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
//
//        // Act
//        ResponseEntity<?> response = profileService.modifyUser(mockFile, mockUser, "test@example.com", "profile_pictures");
//
//        // Assert
//        assertEquals(500, response.getStatusCodeValue());
//        assertEquals("File upload failed: Upload failed", response.getBody());
//    }
//
//    @Test
//    void testChangePassword_Success() {
//        // Arrange
//        PasswordChangeRequest request = new PasswordChangeRequest("oldPassword", "newPassword");
//        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
//        when(passwordEncoder.matches("oldPassword", mockUser.getPassword())).thenReturn(true);
//        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
//
//        // Act
//        String result = profileService.changePassword(request, "test@example.com");
//
//        // Assert
//        assertEquals("Password changed successfully", result);
//        verify(userRepository).save(mockUser);
//    }
//
//    @Test
//    void testChangePassword_Failure_InvalidCurrentPassword() {
//        // Arrange
//        PasswordChangeRequest request = new PasswordChangeRequest("wrongPassword", "newPassword");
//        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
//        when(passwordEncoder.matches("wrongPassword", mockUser.getPassword())).thenReturn(false);
//
//        // Act
//        String result = profileService.changePassword(request, "test@example.com");
//
//        // Assert
//        assertEquals("Unauthorized operations", result);
//        verify(userRepository, never()).save(mockUser);
//    }
//}
