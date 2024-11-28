package com.g02.handyShare.Config.Firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FirebaseServiceTest {

    private FirebaseService firebaseService;

    @Mock
    private Storage storage;

    @Mock
    private Blob blob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        firebaseService = new FirebaseService();
    }

    @Test
    void uploadFile_Success() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Sample Content".getBytes());
        String path = "uploads";

        // Mock the GoogleCredentials
        GoogleCredentials mockCredentials = mock(GoogleCredentials.class);
        BlobId mockBlobId = BlobId.of("handyshare-firebase.appspot.com", path + "/test.txt");
        BlobInfo mockBlobInfo = BlobInfo.newBuilder(mockBlobId).build();

        // Mock the storage interaction
        when(storage.create(mockBlobInfo, mockFile.getBytes())).thenReturn(blob);

        String result = firebaseService.uploadFile(mockFile, path);

        assertTrue(result.contains("https://storage.googleapis.com/handyshare-firebase.appspot.com"));
    }

    @Test
    void downloadFile_FileNotFound() throws IOException {
        String path = "uploads/invalid.txt";

        when(storage.get("handyshare-firebase.appspot.com", path)).thenReturn(null);

        ResponseEntity<byte[]> response = firebaseService.downloadFile(path);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

}