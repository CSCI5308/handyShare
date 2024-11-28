package com.g02.handyShare.Config.Firebase;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerTest {

    @InjectMocks
    private Controller controller;

    @Mock
    private FirebaseService firebaseService;

    public ControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadFile_Success() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Sample Content".getBytes());
        String path = "uploads";
        String expectedUrl = "https://storage.googleapis.com/handyshare-firebase.appspot.com/uploads/test.txt";

        when(firebaseService.uploadFile(mockFile, path)).thenReturn(expectedUrl);

        ResponseEntity<String> response = controller.uploadFile(mockFile, path);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedUrl, response.getBody());
    }

    @Test
    void uploadFile_Failure() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Sample Content".getBytes());
        String path = "uploads";

        when(firebaseService.uploadFile(mockFile, path)).thenThrow(new IOException("Upload failed"));

        ResponseEntity<String> response = controller.uploadFile(mockFile, path);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("File upload failed"));
    }

    @Test
    void downloadFile_Success() throws IOException {
        String path = "uploads/test.txt";
        byte[] fileContent = "Sample Content".getBytes();
        ResponseEntity<byte[]> expectedResponse = ResponseEntity.ok(fileContent);

        when(firebaseService.downloadFile(path)).thenReturn(expectedResponse);

        ResponseEntity<byte[]> response = controller.downloadFile(path);

        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(fileContent, response.getBody());
    }

    @Test
    void downloadFile_Failure() throws IOException {
        String path = "invalid/test.txt";

        when(firebaseService.downloadFile(path)).thenThrow(new IOException("File not found"));

        ResponseEntity<byte[]> response = controller.downloadFile(path);

        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}