package com.g02.handyShare.Config.Firebase;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FirebaseServiceTest {

    @InjectMocks
    private FirebaseService firebaseService;

    @Mock
    private Storage storage;

    @Mock
    private Blob blob;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private InputStream serviceAccountInputStream;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadFile_Success() throws Exception {
        // Mocking the file and blob
        when(multipartFile.getOriginalFilename()).thenReturn("test-file.jpg");
        when(multipartFile.getBytes()).thenReturn("test content".getBytes());
        when(storage.create(any(), any(byte[].class))).thenReturn(blob);
        when(blob.getContentType()).thenReturn("image/jpeg");

        // Simulate a successful file upload
        String uploadedUrl = firebaseService.uploadFile(multipartFile, "product_images");

        // Assert that the URL is generated
        assertNotNull(uploadedUrl);
        assertTrue(uploadedUrl.contains("handyshare-firebase.appspot.com"));
    }

    @Test
    void testUploadFile_ServiceAccountNotFound() {
        // Mock the getResourceAsStream call to return null when the service account is missing
        when(firebaseService.getClass().getClassLoader().getResourceAsStream("firebase-service-account.json")).thenReturn(null);

        // Check if IOException is thrown
        assertThrows(IOException.class, () -> {
            firebaseService.uploadFile(multipartFile, "product_images");
        });
    }

    @Test
    void testDownloadFile_FileNotFound() throws IOException {
        // Mocking the blob to simulate the file not found scenario
        when(storage.get(anyString(), anyString())).thenReturn(null);

        ResponseEntity<byte[]> response = firebaseService.downloadFile("non-existing-file.jpg");

        // Assert that the response is 404 Not Found
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDownloadFile_Success() throws IOException {
        // Mocking the behavior of storage and blob
        when(storage.get(anyString(), anyString())).thenReturn(blob);
        when(blob.exists()).thenReturn(true);
        when(blob.getContentType()).thenReturn("image/jpeg");
        when(blob.getName()).thenReturn("test-file.jpg");

        byte[] content = "test content".getBytes();

        // Using doAnswer for void methods
        doAnswer(invocation -> {
            ByteArrayOutputStream outputStream = invocation.getArgument(0);
            outputStream.write(content);
            return null;
        }).when(blob).downloadTo(any());

        // Calling the method under test
        ResponseEntity<byte[]> response = firebaseService.downloadFile("test-file.jpg");

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertArrayEquals(content, response.getBody());
    }

    @Test
    void testDownloadFile_ServiceAccountNotFound() {
        // Mock the getResourceAsStream call to return null when the service account is missing
        when(firebaseService.getClass().getClassLoader().getResourceAsStream("firebase-service-account.json")).thenReturn(null);

        // Check if IOException is thrown
        assertThrows(IOException.class, () -> {
            firebaseService.downloadFile("test-file.jpg");
        });
    }
}
