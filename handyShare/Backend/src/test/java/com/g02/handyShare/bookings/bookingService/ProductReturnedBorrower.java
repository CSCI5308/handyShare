package com.g02.handyShare.bookings.bookingService;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Optional;

import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.Product.Service.CustomException;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.repository.BookingRepository;
import com.g02.handyShare.bookings.service.BookingService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class ProductReturnedBorrower {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FirebaseService firebaseService;
    public void Controller(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }
    @Mock
    private MultipartFile file;

    private Bookings borrowInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Explicitly set the FirebaseService in BookingService
        bookingService.Controller(firebaseService);

        // Initialize borrow instance
        borrowInstance = new Bookings();
        borrowInstance.setId(1L);
    }

    // Test case 1: Product successfully marked as returned
    @Test
    public void productReturnedBorrower_success() throws IOException {
        // Arrange
        String mockImageUrl = "https://firebase.com/returnProofByBorrower/image.jpg";
        when(firebaseService.uploadFile(file, "/returnProofByBorrower")).thenReturn(mockImageUrl);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(borrowInstance));

        // Act
        ResponseEntity<?> response = bookingService.productReturnedBorrower(1L, file);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Product successfully marked as return.", response.getBody());
        assertEquals(mockImageUrl, borrowInstance.getReturnImage());
        assertNotNull(borrowInstance.getReturnByBorrowerTime());

        // Verify interactions
        verify(firebaseService, times(1)).uploadFile(file, "/returnProofByBorrower");
        verify(bookingRepository, times(1)).save(borrowInstance);
    }

    // Test case 2: Booking not found
    @Test(expected = CustomException.class)
    public void productReturnedBorrower_bookingNotFound() throws IOException {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        bookingService.productReturnedBorrower(1L, file);
    }

    // Test case 3: File upload fails
    @Test(expected = IOException.class)
    public void productReturnedBorrower_fileUploadFails() throws IOException {
        // Arrange
        when(firebaseService.uploadFile(file, "/returnProofByBorrower")).thenThrow(new IOException("File upload failed"));

        // Act
        bookingService.productReturnedBorrower(1L, file);
    }

    // Test case 4: Null file upload
    @Test(expected = CustomException.class)
    public void productReturnedBorrower_nullFile() throws IOException {
        // Act
        bookingService.productReturnedBorrower(1L, null);
    }

    // Test case 5: Empty file upload
    @Test(expected = IOException.class)
    public void productReturnedBorrower_emptyFile() throws IOException {
        // Arrange
        when(file.isEmpty()).thenReturn(true);
        when(firebaseService.uploadFile(file, "/returnProofByBorrower"))
                .thenThrow(new IOException("Empty file provided"));

        // Act
        bookingService.productReturnedBorrower(1L, file);
    }
}
