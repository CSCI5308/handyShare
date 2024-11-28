package com.g02.handyShare.bookings;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.g02.handyShare.bookings.service.PriceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.repository.BookingRepository;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class PriceCalculatorTest {

    @InjectMocks
    private PriceCalculator priceCalculator;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }



    @Test
    void testCalculatePayment_NoPenalty_ReturnedOnTime() {
        // Arrange
        LocalDateTime timerEnd = LocalDateTime.now().plusHours(2);
        LocalDateTime returnTime = timerEnd; // Returned exactly on time
        Bookings booking = new Bookings();
        booking.setDuration(2);
        booking.setPenalty(0.0);

        Product product = new Product();
        product.setRentalPrice(50.0);

        when(bookingRepository.save(booking)).thenReturn(booking);
        // Act
        double totalPrice = priceCalculator.calculatePayment(returnTime, timerEnd, booking, product);

        // Assert
        double expectedPrice = 2 * 50.0 + 2 * 50.0 * 0.02;
        assertEquals(expectedPrice, totalPrice, 0.01);
        assertEquals(0.0, booking.getPenalty());
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void testCalculatePayment_PenaltyWithinGracePeriod() {
        // Arrange
        LocalDateTime timerEnd = LocalDateTime.now().plusHours(2);
        LocalDateTime returnTime = timerEnd.plusMinutes(20); // Within grace period
        Bookings booking = new Bookings();
        booking.setDuration(2);
        booking.setPenalty(0.0);

        Product product = new Product();
        product.setRentalPrice(50.0);

        // Act
        double totalPrice = priceCalculator.calculatePayment(returnTime, timerEnd, booking, product);

        // Assert
        double expectedPrice = 2 * 50.0 + 2 * 50.0 * 0.02;
        assertEquals(expectedPrice, totalPrice, 0.01);
        assertEquals(0.0, booking.getPenalty());
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void testCalculatePayment_PenaltyBeyondGracePeriod() {
        // Arrange
        Product product = new Product();
        product.setRentalPrice(50.0);

        LocalDateTime timerEnd = LocalDateTime.now().plusHours(2);
        LocalDateTime returnTime = timerEnd.plusMinutes(90); // 1 hour after grace period
        Bookings booking = new Bookings();
        booking.setDuration(2);
        booking.setPenalty(0.0);
        booking.setProduct(product);
        booking.setTimerEnd(timerEnd);
        booking.setReturnDateTime(returnTime);

        when(bookingRepository.save(booking)).thenReturn(booking);

        // Act
        double totalPrice = priceCalculator.calculatePayment(returnTime, timerEnd, booking, product);

        // Assert
        long extraHours = 1; // Rounded up
        double expectedPenalty = (50.0 +( 0.05 * 50.0)) * extraHours;
        double expectedPrice = 155.55  ; //including 2 % platform fees
     assertEquals(expectedPrice, totalPrice, 0.01);
        assertEquals(expectedPenalty, booking.getPenalty());

    }



    @Test
    void testCalculatePayment_PenaltyJustAfterGracePeriod() {
        // Arrange
        LocalDateTime timerEnd = LocalDateTime.now().plusHours(2);
        LocalDateTime returnTime = timerEnd.plusMinutes(31); // Just 1 minute after grace period
        Bookings booking = new Bookings();
        booking.setDuration(2);
        booking.setPenalty(0.0);

        Product product = new Product();
        product.setRentalPrice(80.0);

        // Act
        double totalPrice = priceCalculator.calculatePayment(returnTime, timerEnd, booking, product);

        // Assert
        long extraHours = 1; // Minimum penalty
        double expectedPenalty = (80.0 + 0.05 * 80.0) * extraHours;
        double expectedPrice = 248.88;
        assertEquals(expectedPrice, totalPrice, 0.01);
        assertEquals(expectedPenalty, booking.getPenalty());
        verify(bookingRepository, times(1)).save(booking);
    }


}
