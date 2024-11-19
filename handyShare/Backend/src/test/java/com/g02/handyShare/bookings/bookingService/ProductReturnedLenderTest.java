package com.g02.handyShare.bookings.bookingService;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.g02.handyShare.Product.Service.CustomException;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.service.BookingService;
import com.g02.handyShare.bookings.repository.BookingRepository;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;

import com.g02.handyShare.bookings.service.PriceCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class ProductReturnedLenderTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PriceCalculator calculator;

    private Bookings testBooking;
    private Product testProduct;

    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Set up test data
        testBooking = new Bookings();
        testBooking.setId(1L);
        testBooking.setTimerEnd(LocalDateTime.now().plusHours(2));

        User lender = new User();
        lender.setId(1L);
        lender.setName("Lender Name");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setAvailable(false);
        testProduct.setLender(lender);
        testBooking.setProduct(testProduct);

        // Mocking methods for finding bookings and products
        when(bookingRepository.findById(testBooking.getId())).thenReturn(Optional.of(testBooking));
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
    }

    // Test case 1: Product returned successfully without penalty
    @Test
    public void productReturnedLender_successfulReturn_noPenalty() {
        // Arrange
        double calculatedPrice = 100.0;
        when(calculator.calculatePayment(any(LocalDateTime.class), any(LocalDateTime.class), eq(testBooking), eq(testProduct)))
                .thenReturn(calculatedPrice);

        // Act
        ResponseEntity<?> response = bookingService.productReturnedLender(testBooking.getId());

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("has been returned"));
     }

    // Test case 2: Product returned with penalty
    @Test
    public void productReturnedLender_successfulReturn_withPenalty() {
        // Arrange
        testBooking.setPenalty(20.0);
        double calculatedPrice = 120.0;
        when(calculator.calculatePayment(any(LocalDateTime.class), any(LocalDateTime.class), eq(testBooking), eq(testProduct)))
                .thenReturn(calculatedPrice);

        // Act
        ResponseEntity<?> response = bookingService.productReturnedLender(testBooking.getId());

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("has been returned"));
        assertTrue(response.getBody().toString().contains("with a late return penalty of $20.0"));
    }

    // Test case 3: Booking not found
    @Test(expected = CustomException.class)
    public void productReturnedLender_bookingNotFound() {
        // Arrange
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        bookingService.productReturnedLender(99L);  // Non-existent booking ID
    }

    // Test case 4: Product not found
    @Test(expected = CustomException.class)
    public void productReturnedLender_productNotFound() {
        // Arrange
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.empty());

        // Act
        bookingService.productReturnedLender(testBooking.getId());
    }

    // Test case 5: Null product in booking (edge case)
    @Test(expected = NullPointerException.class)
    public void productReturnedLender_nullProduct() {
        // Arrange
        testBooking.setProduct(null);

        // Act
        bookingService.productReturnedLender(testBooking.getId());
    }


}
