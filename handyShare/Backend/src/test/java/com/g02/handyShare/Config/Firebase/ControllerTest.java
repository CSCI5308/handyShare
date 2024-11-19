// package com.g02.handyShare.Config.Firebase;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.web.multipart.MultipartFile;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @ExtendWith(SpringExtension.class)  // Ensures Spring context is used
// @WebMvcTest(Controller.class)  // Only test the Controller class
// class ControllerTest {

//     @Autowired
//     private MockMvc mockMvc;  // MockMvc to simulate HTTP requests

//     @Mock
//     private FirebaseService firebaseService;  // Mock FirebaseService

//     @InjectMocks
//     private Controller controller;  // Inject mocks into the Controller

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);  // Initialize mocks before each test
//     }

//     @Test
//     void testUploadFile_Success() throws Exception {
//         // Mock successful file upload
//         when(firebaseService.uploadFile(any(MultipartFile.class), any(String.class)))
//                 .thenReturn("File uploaded successfully");

//         // Simulate a file upload request
//         mockMvc.perform(multipart("/api/v1/all/upload")
//                         .file("file", "test content".getBytes())  // Mock file data
//                         .param("path", "product_images"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("File uploaded successfully"));
//     }

//     @Test
//     void testUploadFile_Failure() throws Exception {
//         // Simulate an IOException during file upload
//         when(firebaseService.uploadFile(any(MultipartFile.class), any(String.class)))
//                 .thenThrow(new IOException("File upload failed"));

//         // Simulate the file upload request and expect an error
//         mockMvc.perform(multipart("/api/v1/all/upload")
//                         .file("file", "test content".getBytes())  // Mock file data
//                         .param("path", "product_images"))
//                 .andExpect(status().isInternalServerError())
//                 .andExpect(content().string("File upload failed: File upload failed"));
//     }

//     @Test
//     void testDownloadFile_Success() throws Exception {
//         byte[] fileContent = "test content".getBytes();

//         // Mock successful file download
//         when(firebaseService.downloadFile(any(String.class)))
//                 .thenReturn(ResponseEntity.ok().body(fileContent));

//         // Simulate the file download request
//         mockMvc.perform(get("/download")
//                         .param("path", "product_images/test-file.jpg"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
//                 .andExpect(content().bytes(fileContent));
//     }

//     @Test
//     void testDownloadFile_Failure() throws Exception {
//         // Simulate an IOException during file download
//         when(firebaseService.downloadFile(any(String.class)))
//                 .thenThrow(new IOException("File not found"));

//         // Simulate the file download request and expect an error
//         mockMvc.perform(get("/download")
//                         .param("path", "product_images/non-existing-file.jpg"))
//                 .andExpect(status().isInternalServerError())
//                 .andExpect(content().bytes(null));  // No content returned
//     }
// }
