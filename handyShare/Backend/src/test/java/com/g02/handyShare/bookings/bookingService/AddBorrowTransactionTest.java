package com.g02.handyShare.bookings.bookingService;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.g02.handyShare.Product.Service.CustomException;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.bookings.service.BookingService;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.repository.BookingRepository;
 import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class AddBorrowTransactionTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ProductRepository productRepository;

    private User testUser;
    private Product testProduct;
    private Bookings testBooking;

    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Set up SecurityContext to return the mocked Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mocking authentication to return a valid email
        when(authentication.getName()).thenReturn("test@handyshare.com");

        // Initialize test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@handyshare.com");

        testProduct = new Product();
        testProduct.setId(4L);
        testProduct.setLender(testUser);
        testProduct.setAvailable(true);

        testBooking = new Bookings();
        testBooking.setId(2L);
        testBooking.setProduct(testProduct);
        testBooking.setBorrower(testUser);
        testBooking.setTimerStart(LocalDateTime.now());
        testBooking.setDuration(2);

        // Mock repository calls
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        when(bookingRepository.save(testBooking)).thenReturn(testBooking);
        when(productRepository.save(testProduct)).thenReturn(testProduct);
    }

    // Test case 1: Successful booking
    @Test
    public void addBorrowTransaction_successful() {
        // Arrange
        LocalDateTime timerStart = testBooking.getTimerStart();
        LocalDateTime endTimer = timerStart.plusHours(testBooking.getDuration());

        // Mock no overlap for successful booking
        when(bookingRepository.existsByBorrowerAndProductAndOverlappingTime(
                testProduct, timerStart, endTimer))
                .thenReturn(false);

        // Act
        Bookings result = bookingService.addBorrowTransaction(testBooking);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getBorrower().getEmail());
        assertEquals(testProduct.getId(), result.getProduct().getId());
        assertEquals(endTimer, result.getTimerEnd());
    }

    // Test case 2: Product not found
    @Test(expected = CustomException.class)
    public void addBorrowTransaction_productNotFound() {
        // Arrange
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.empty());

        // Act
        bookingService.addBorrowTransaction(testBooking);
    }

    // Test case 3: Start time is null
    @Test(expected = CustomException.class)
    public void addBorrowTransaction_startTimeIsNull() {
        // Arrange
        testBooking.setTimerStart(null);

        // Act
        bookingService.addBorrowTransaction(testBooking);
    }

    // Test case 4: Overlapping booking exists
    @Test(expected = CustomException.class)
    public void addBorrowTransaction_overlappingBookingExists() {
        // Arrange
        LocalDateTime timerStart = testBooking.getTimerStart();
        LocalDateTime endTimer = timerStart.plusHours(testBooking.getDuration());

        // Mock overlap exists
        when(bookingRepository.existsByBorrowerAndProductAndOverlappingTime(
                testProduct, timerStart, endTimer))
                .thenReturn(true);

        // Act
        bookingService.addBorrowTransaction(testBooking);
    }

    // Test case 5: Borrower is correctly set
    @Test
    public void addBorrowTransaction_borrowerIsSetCorrectly() {
        // Arrange
        LocalDateTime timerStart = testBooking.getTimerStart();
        LocalDateTime endTimer = timerStart.plusHours(testBooking.getDuration());

        // Mock no overlap
        when(bookingRepository.existsByBorrowerAndProductAndOverlappingTime(
                testProduct, timerStart, endTimer))
                .thenReturn(false);

        // Act
        Bookings result = bookingService.addBorrowTransaction(testBooking);

        // Assert
        assertEquals(testUser, result.getBorrower());
    }

    // Test case 6: End time is correctly calculated
    @Test
    public void addBorrowTransaction_endTimeCalculation() {
        // Arrange
        LocalDateTime timerStart = testBooking.getTimerStart();
        int duration = testBooking.getDuration();
        LocalDateTime expectedEndTime = timerStart.plusHours(duration);

        // Mock no overlap
        when(bookingRepository.existsByBorrowerAndProductAndOverlappingTime(
                testProduct, timerStart, expectedEndTime))
                .thenReturn(false);

        // Act
        Bookings result = bookingService.addBorrowTransaction(testBooking);

        // Assert
        assertEquals(expectedEndTime, result.getTimerEnd());
    }
}
