package com.g02.handyShare.bookings;

 

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.repository.BookingRepository;
import com.g02.handyShare.bookings.service.AvailabilityTimer;
@RunWith(MockitoJUnitRunner.class)
class AvailabilityTimerTest {

    @InjectMocks
    private AvailabilityTimer availabilityTimer;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckAndUpdateAvailability_ProductSetUnavailable() {
        // Arrange
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startTime = currentTime.minusMinutes(5);
        LocalDateTime endTime = currentTime.plusMinutes(5);

        Product product = new Product();
        product.setId(1L);
        product.setAvailable(true);

        Bookings booking = new Bookings();
        booking.setProduct(product);
        booking.setTimerStart(startTime);
        booking.setTimerEnd(endTime);
        booking.setReturnDateTime(null);

        when(bookingRepository.findAll()).thenReturn(Collections.singletonList(booking));

        // Act
        availabilityTimer.checkAndUpdateAvailability();

        // Assert
        assertFalse(product.getAvailable(), "Product should be set to unavailable");
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testCheckAndUpdateAvailability_NoChangeIfOutsideTimeWindow() {
        // Arrange
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startTime = currentTime.plusMinutes(5);
        LocalDateTime endTime = currentTime.plusMinutes(10);

        Product product = new Product();
        product.setId(2L);
        product.setAvailable(true);

        Bookings booking = new Bookings();
        booking.setProduct(product);
        booking.setTimerStart(startTime);
        booking.setTimerEnd(endTime);
        booking.setReturnDateTime(null);

        when(bookingRepository.findAll()).thenReturn(Collections.singletonList(booking));

        // Act
        availabilityTimer.checkAndUpdateAvailability();

        // Assert
        assertTrue(product.getAvailable(), "Product should remain available");
        verify(productRepository, never()).save(product);
    }

    @Test
    void testCheckAndUpdateAvailability_SkipsIfReturnDateTimeExists() {
        // Arrange
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startTime = currentTime.minusMinutes(5);
        LocalDateTime endTime = currentTime.plusMinutes(5);

        Product product = new Product();
        product.setId(3L);
        product.setAvailable(true);

        Bookings booking = new Bookings();
        booking.setProduct(product);
        booking.setTimerStart(startTime);
        booking.setTimerEnd(endTime);
        booking.setReturnDateTime(currentTime);

        when(bookingRepository.findAll()).thenReturn(Collections.singletonList(booking));

        // Act
        availabilityTimer.checkAndUpdateAvailability();

        // Assert
        assertTrue(product.getAvailable(), "Product should remain available if returnDateTime exists");
        verify(productRepository, never()).save(product);
    }

    @Test
    void testCheckAndUpdateAvailability_EmptyBookingsList() {
        // Arrange
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        availabilityTimer.checkAndUpdateAvailability();

        // Assert
        verify(productRepository, never()).save(any());
    }
}
