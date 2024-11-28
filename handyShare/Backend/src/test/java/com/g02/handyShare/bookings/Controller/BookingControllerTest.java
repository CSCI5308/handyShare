package com.g02.handyShare.bookings.Controller;

import com.g02.handyShare.bookings.controller.BookingController;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mocking SecurityContextHolder to simulate an authenticated user
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testBorrowItem_success() {
        Bookings borrowInstance = new Bookings();
        borrowInstance.setTimerStart(LocalDateTime.now());
        borrowInstance.setDuration(24);

        // Mock the service method
        when(bookingService.addBorrowTransaction(any(Bookings.class))).thenReturn(borrowInstance);

        // Call the controller method
        ResponseEntity<?> response = bookingController.borrowItem(borrowInstance);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testProductReturnedLender_success() {
        Long borrowId = 1L;
        Map<String, Long> requestBody = Map.of("borrowId", borrowId);

        // Mock the service method using doReturn
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Returned successfully");
        doReturn(mockResponse).when(bookingService).productReturnedLender(borrowId);

        // Call the controller method
        ResponseEntity<?> response = bookingController.productReturnedLender(requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Returned successfully", response.getBody());
    }

    @Test
    public void testProductReturnedLender_borrowIdMissing() {
        Map<String, Long> requestBody = Map.of();

        // Call the controller method
        ResponseEntity<?> response = bookingController.productReturnedLender(requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Borrow ID is missing in the request", response.getBody());
    }

    @Test
    public void testProductReturnedBorrower_success() throws IOException {
        Long borrowId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[1]);

        // Mock the service method using doReturn
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Product successfully marked as return.");
        doReturn(mockResponse).when(bookingService).productReturnedBorrower(borrowId, file);

        // Call the controller method
        ResponseEntity<?> response = bookingController.productReturnedBorrower(borrowId, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product successfully marked as return.", response.getBody());
    }

    @Test
    public void testProductReturnedBorrower_borrowIdMissing() throws IOException {
        Long borrowId = null;
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[1]);

        // Call the controller method
        ResponseEntity<?> response = bookingController.productReturnedBorrower(borrowId, file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Borrow ID is missing in the request", response.getBody());
    }

    @Test
    public void testGetBorrowedList_success() {
        // Mock the service method
        when(bookingService.getBorrowedItems()).thenReturn(Collections.emptyList());

        // Call the controller method
        List<Bookings> response = bookingController.getBorrowedList();

        assertEquals(0, response.size());
        assertNotNull(response);
    }

    @Test
    public void testGetLendedItems_success() {
        // Mock the service method to return an empty list
        when(bookingService.getLendedItems()).thenReturn(Collections.emptyList());

        // Call the controller method
        List<Bookings> response = bookingController.getLendedItems(); // No need for ResponseEntity casting

        // Assert the response is correct
        assertNotNull(response);  // Ensure the response is not null
        assertEquals(0, response.size());
    }
}
